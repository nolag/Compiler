package cs444.codegen.x86_32bit;

import cs444.codegen.IRuntime;
import cs444.codegen.instructions.x86.Call;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Dec;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.Loop;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.Sar;
import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.instructions.x86.Xor;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.SizeHelper;

public class Runtime implements IRuntime<X86Instruction>{
    public static final String EXCEPTION_LBL = "__exception";

    private static final Immediate MALLOC = new Immediate("__malloc");
    private static final Immediate EXCEPTION = new Immediate(EXCEPTION_LBL);

    private static long loopnum = 0;

    public static final Runtime instance = new Runtime();

    private static long getLoopNum(){
        return loopnum++;
    }

    private Runtime(){ }

    @Override
    public void malloc(final InstructionArg bytes, final InstructionHolder<X86Instruction> instructions,
            final Size size, final boolean zeroOut) {

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

    @Override
    public void malloc(final InstructionArg bytes, final InstructionHolder<X86Instruction> instructions, final Size size){
        malloc(bytes, instructions, size, true);
    }

    @Override
    public void externAll(final InstructionHolder<X86Instruction> instructions) {
        instructions.add(new Extern(MALLOC));
        instructions.add(new Extern(EXCEPTION));
    }

    @Override
    public void throwException(final InstructionHolder<X86Instruction> instructions, final String debugString){
        instructions.add(new Comment(debugString));
        instructions.add(new Call(EXCEPTION));
    }
}
