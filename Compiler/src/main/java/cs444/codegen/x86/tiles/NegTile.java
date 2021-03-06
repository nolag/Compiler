package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Neg;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class NegTile extends NumericHelperTile<X86Instruction, Size, NegOpExprSymbol> {
    private static NegTile tile;

    private NegTile() {}

    public static NegTile getTile() {
        if (tile == null) {
            tile = new NegTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(NegOpExprSymbol negSymbol,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(negSymbol.children.get(0)));
        instructions.add(new Neg(Register.ACCUMULATOR, sizeHelper));
        return instructions;
    }
}
