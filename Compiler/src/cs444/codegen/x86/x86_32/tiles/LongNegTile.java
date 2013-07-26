package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Adc;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Not;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class LongNegTile extends LongOnlyTile<NegOpExprSymbol>{
    public static void init(){
        new LongNegTile();
    }

    private LongNegTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).negs.add(this);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final NegOpExprSymbol negSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(negSymbol.children.get(0)));
        instructions.add(new Comment("Start long neg"));
        instructions.add(new Not(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Not(Register.DATA, sizeHelper));
        instructions.add(new Comment("Make one's complement into two and set carry"));
        instructions.add(new Add(Register.ACCUMULATOR, Immediate.ONE, sizeHelper));
        instructions.add(new Comment("Use carry to finish complement"));
        instructions.add(new Adc(Register.DATA, Immediate.ZERO, sizeHelper));
        instructions.add(new Comment("End long neg"));
        return instructions;
    }

}
