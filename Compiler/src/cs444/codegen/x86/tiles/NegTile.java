package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Neg;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericHelperTile;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class NegTile extends NumericHelperTile<NegOpExprSymbol>{
    private static NegTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new NegTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).negs.add(tile);
    }

    private NegTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final NegOpExprSymbol negSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(negSymbol.children.get(0)));
        instructions.add(new Neg(Register.ACCUMULATOR, sizeHelper));
        return instructions;
    }

}
