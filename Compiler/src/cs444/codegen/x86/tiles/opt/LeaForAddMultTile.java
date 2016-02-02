package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.codegen.x86.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.MultiplyMemoryFormat;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Lea;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class LeaForAddMultTile implements ITile<X86Instruction, Size, MultiplyExprSymbol> {
    private static LeaForAddMultTile tile;

    public static LeaForAddMultTile getTile() {
        if(tile == null) tile = new LeaForAddMultTile();
        return tile;
    }

    private LeaForAddMultTile() { }

    @Override
    public boolean fits(final MultiplyExprSymbol op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        if (!(sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName))) return false;
        return TileHelper.powerTwoOrNull(op.getChildren().get(0), 8) != null || TileHelper.powerTwoOrNull(op.children.get(1), 8) != null;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final MultiplyExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final ISymbol left = symbol.getChildren().get(0);
        final ISymbol right = symbol.getChildren().get(1);

        final ISymbol multWith;
        long value;
        if (TileHelper.powerTwoOrNull(left, 8) != null) {
            multWith = right;
            value = TileHelper.getValue(left);
        } else {
            multWith = left;
            value = TileHelper.getValue(right);
        }

        instructions.addAll(platform.getBest(multWith));
        instructions.add(new Lea(Register.ACCUMULATOR, new Memory(new MultiplyMemoryFormat(Register.ACCUMULATOR, (int)value)), sizeHelper));

        return instructions;
    }
}
