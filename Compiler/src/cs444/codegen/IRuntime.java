package cs444.codegen;

import cs444.codegen.instructions.Instruction;

public interface IRuntime<T extends Instruction<T>> {
    void mallocClear(final Addable<T> instructions);
    void mallocNoClear(final Addable<T> instructions);
    void throwException(final Addable<T> instructions, final String debugString);
    void externAll(final Addable<T> instructions);
}
