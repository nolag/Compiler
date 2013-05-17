package cs444.codegen.x86_32.linux;

import cs444.codegen.IRuntime;
import cs444.codegen.instructions.x86.Call;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.X86SizeHelper;

public class Runtime implements IRuntime<X86Instruction>{
    public static final String EXCEPTION_LBL = "__exception";

    private static final Immediate MALLOC = new Immediate("__malloc");
    private static final Immediate MALLOC_CLEAR = new Immediate("__malloc_clear");
    private static final Immediate EXCEPTION = new Immediate(EXCEPTION_LBL);

    public static final Runtime instance = new Runtime();

    private Runtime(){ }

    @Override
    public void mallocClear(final InstructionHolder<X86Instruction> instructions) {
        instructions.add(new Call(MALLOC_CLEAR, X86SizeHelper.sizeHelper32));
    }

    @Override
    public void mallocNoClear(final InstructionHolder<X86Instruction> instructions){
        instructions.add(new Call(MALLOC, X86SizeHelper.sizeHelper32));
    }

    @Override
    public void externAll(final InstructionHolder<X86Instruction> instructions) {
        instructions.add(new Extern(MALLOC));
        instructions.add(new Extern(EXCEPTION));
    }

    @Override
    public void throwException(final InstructionHolder<X86Instruction> instructions, final String debugString){
        instructions.add(new Comment(debugString));
        instructions.add(new Call(EXCEPTION, X86SizeHelper.sizeHelper32));
    }
}
