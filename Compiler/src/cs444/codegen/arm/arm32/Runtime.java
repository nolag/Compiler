package cs444.codegen.arm.arm32;

import cs444.codegen.Addable;
import cs444.codegen.IRuntime;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.instructions.Bl;
import cs444.codegen.arm.instructions.Extern;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class Runtime implements IRuntime<ArmInstruction> {
    private static final ImmediateStr MALLOC = new ImmediateStr("__malloc");
    private static final ImmediateStr MALLOC_CLEAR = new ImmediateStr("__malloc_clear");
    private static final ImmediateStr EXCEPTION = new ImmediateStr("__exception");

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
        instructions.add(new Extern(MALLOC));
        instructions.add(new Extern(MALLOC_CLEAR));
        instructions.add(new Extern(EXCEPTION));
    }
}
