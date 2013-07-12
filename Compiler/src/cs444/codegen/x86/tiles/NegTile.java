package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Neg;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class NegTile implements ITile<X86Instruction, Size, NegOpExprSymbol>{
    public static void init(){
        new NegTile();
    }

    private NegTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).negs.add(this);
    }

    @Override
    public boolean fits(final NegOpExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return true;
    }

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
