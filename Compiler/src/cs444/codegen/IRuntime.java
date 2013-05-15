package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public interface IRuntime<T extends Instruction> {
    void malloc(final InstructionArg bytes, final InstructionHolder<T> instructions, final Size size);
    void malloc(final InstructionArg bytes, final InstructionHolder<T> instructions, final Size size, final boolean zeroOut);
    void throwException(final InstructionHolder<T> instructions, final String debugString);
    void externAll(final InstructionHolder<T> instructions);
}
