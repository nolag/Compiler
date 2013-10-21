package cs444.codegen.x86.tiles.opt;

import static cs444.codegen.x86.tiles.helpers.X86TileHelper.getIntValue;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.MultiplyMemoryFormat;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Lea;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class LeaForMultTile implements ITile<X86Instruction, Size, MultiplyExprSymbol> {
    private static LeaForMultTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new LeaForMultTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).mults.add(tile);
    }

    private LeaForMultTile() { }

    @Override
    public boolean fits(final MultiplyExprSymbol op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        if (!(sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName))) return false;
        return getIntValue(op.getChildren().get(0), 9, 0) != null || getIntValue(op.children.get(1), 9, 0) != null;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final MultiplyExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final ISymbol left = symbol.getChildren().get(0);
        final ISymbol right = symbol.getChildren().get(1);

        final ISymbol multWith;
        Integer value;
        if ((value = getIntValue(left, 9, 0)) != null) {
            multWith = right;
        } else {
            multWith = left;
            value = getIntValue(right, 9, 0);
        }

        instructions.addAll(platform.getBest(multWith));
        instructions.add(new Lea(Register.ACCUMULATOR, new Memory(new MultiplyMemoryFormat(Register.ACCUMULATOR, value)), sizeHelper));

        return instructions;
    }
}
