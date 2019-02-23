package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileHelper;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class LongNegTile extends LongOnlyTile<X86Instruction, Size, NegOpExprSymbol> {
    private static LongNegTile tile;

    public static LongNegTile getTile() {
        if (tile == null) tile = new LongNegTile();
        return tile;
    }

    private LongNegTile() {}

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final NegOpExprSymbol negSymbol, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();

        final Typeable neg = (Typeable) negSymbol.children.get(0);
        instructions.addAll(platform.getBest(neg));
        tileHelper.makeLong(neg, instructions, sizeHelper);
        X86_32TileHelper.negLong(Register.DATA, Register.ACCUMULATOR, instructions, sizeHelper);
        return instructions;
    }

}
