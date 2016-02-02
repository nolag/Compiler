package cs444.codegen;

import cs444.codegen.instructions.Instruction;

public interface IRuntime<T extends Instruction<T>> {
    public static final String EXCEPTION_LBL = "__exception";
    public static final String MALLOC_LBL = "__malloc";
    public static final String MALLOC_CLEAR_LBL = "__malloc_clear";

    void mallocClear(final Addable<T> instructions);

    void mallocNoClear(final Addable<T> instructions);

    void throwException(final Addable<T> instructions, final String debugString);

    void externAll(final Addable<T> instructions);
}
