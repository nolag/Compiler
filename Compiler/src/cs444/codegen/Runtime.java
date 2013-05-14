package cs444.codegen;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Call;
import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Dec;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Label;
import cs444.codegen.instructions.Loop;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Pop;
import cs444.codegen.instructions.Push;
import cs444.codegen.instructions.Sar;
import cs444.codegen.instructions.Xor;
import cs444.codegen.peephole.InstructionHolder;

public class Runtime {
    public static final String EXCEPTION_LBL = "__exception";

    private static final Immediate MALLOC = new Immediate("__malloc");
    private static final Immediate EXCEPTION = new Immediate(EXCEPTION_LBL);

    private static long loopnum = 0;

    private static long getLoopNum(){
        return loopnum++;
    }

    public static void malloc(final InstructionArg bytes, final InstructionHolder instructions, final Size size, final boolean zeroOut) {
        // backup regs used by malloc
        instructions.add(new Push(Register.BASE));
        if(bytes != Register.COUNTER)instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR));
        instructions.add(new Sar(Register.COUNTER, SizeHelper.getPowerSizeImd(size)));
        instructions.add(new Call(MALLOC));
        instructions.add(new Pop(Register.BASE));

        if(zeroOut){
            final String myLbl = "ZEROLOOP" + getLoopNum();

            instructions.add(new Comment("The number of bytes is from 0 to size - 1"));
            instructions.add(new Dec(Register.COUNTER));

            instructions.add(new Xor(Register.DATA, Register.DATA));
            instructions.add(new Comment("Zeroing out the newly allocated values"));
            instructions.add(new Label(myLbl));
            instructions.add(new Mov(SizeHelper.getZeroImd(size), Register.DATA));
            instructions.add(new Loop(myLbl));
            instructions.add(new Comment("Done zeroing out the newly allocated values"));
        }
    }

    public static void malloc(final InstructionArg bytes, final InstructionHolder instructions, final Size size){
        malloc(bytes, instructions, size, true);
    }

    public static void externAll(final InstructionHolder instructions) {
        instructions.add(new Extern(MALLOC));
        instructions.add(new Extern(EXCEPTION));
    }

    public static void throwException(final InstructionHolder instructions, final String debugString){
        instructions.add(new Comment(debugString));
        instructions.add(new Call(EXCEPTION));
    }
}
