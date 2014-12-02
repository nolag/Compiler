package cs444.codegen.arm.arm32;

import cs444.codegen.Addable;
import cs444.codegen.IRuntime;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.instructions.Bl;
import cs444.codegen.arm.instructions.Extern;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class Runtime implements IRuntime<ArmInstruction> {
    private static final String MALLOC = "__malloc";
    private static final String MALLOC_CLEAR = "__malloc_clear";
    private static final String EXCEPTION = "__exception";

    private static final ImmediateStr MALLOC_IMM = new ImmediateStr(MALLOC);
    private static final ImmediateStr MALLOC_CLEAR_IMM = new ImmediateStr(MALLOC_CLEAR);
    private static final ImmediateStr EXCEPTION_IMM = new ImmediateStr(EXCEPTION);

    public static final Runtime instance = new Runtime();

    private Runtime() {}

    @Override
    public void mallocClear(Addable<ArmInstruction> instructions) {
        instructions.add(new Bl(MALLOC_CLEAR));
    }

    @Override
    public void mallocNoClear(Addable<ArmInstruction> instructions) {
        instructions.add(new Bl(MALLOC));
    }

    @Override
    public void throwException(Addable<ArmInstruction> instructions, String debugString) {
        instructions.add(new Bl(EXCEPTION));
    }

    @Override
    public void externAll(Addable<ArmInstruction> instructions) {
        instructions.add(new Extern(MALLOC_IMM));
        instructions.add(new Extern(MALLOC_CLEAR_IMM));
        instructions.add(new Extern(EXCEPTION_IMM));
    }
}
