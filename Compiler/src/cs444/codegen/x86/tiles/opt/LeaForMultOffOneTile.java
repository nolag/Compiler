package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Lea;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class LeaForMultOffOneTile implements ITile<X86Instruction, Size, MultiplyExprSymbol> {
    private static LeaForMultOffOneTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new LeaForMultOffOneTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).mults.add(tile);
    }

    private LeaForMultOffOneTile() { }

    @Override
    public boolean fits(final MultiplyExprSymbol op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        if (!(sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName))) return false;
        Integer lhs = TileHelper.powerTwoOrNull(op.getChildren().get(0), 8, 1);
        Integer rhs = TileHelper.powerTwoOrNull(op.children.get(1), 8, 1);
        return  lhs != null && lhs != 1 || rhs != null && rhs != 1;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final MultiplyExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final ISymbol left = symbol.getChildren().get(0);
        final ISymbol right = symbol.getChildren().get(1);

        final ISymbol multWith;
        long value;
        if (TileHelper.powerTwoOrNull(left, 8, 1) != null) {
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
