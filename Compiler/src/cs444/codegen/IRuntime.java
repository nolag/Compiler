package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;

public interface IRuntime<T extends Instruction> {
    void mallocClear(final InstructionHolder<T> instructions);
    void mallocNoClear(final InstructionHolder<T> instructions);
    void throwException(final InstructionHolder<T> instructions, final String debugString);
    void externAll(final InstructionHolder<T> instructions);
}
