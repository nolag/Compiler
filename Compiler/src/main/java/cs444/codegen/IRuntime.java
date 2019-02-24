package cs444.codegen;

import cs444.codegen.instructions.Instruction;

public interface IRuntime<T extends Instruction<T>> {
    String EXCEPTION_LBL = "__exception";
    String MALLOC_LBL = "__malloc";
    String MALLOC_CLEAR_LBL = "__malloc_clear";

    void mallocClear(Addable<T> instructions);

    void mallocNoClear(Addable<T> instructions);

    void throwException(Addable<T> instructions, String debugString);

    void externAll(Addable<T> instructions);
}
