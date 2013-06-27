package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.x86.bases.X86Instruction;

public interface IRuntime<T extends Instruction> {
    void mallocClear(final Addable<X86Instruction> instructions);
    void mallocNoClear(final Addable<X86Instruction> instructions);
    void throwException(final Addable<X86Instruction> instructions, final String debugString);
    void externAll(final Addable<X86Instruction> instructions);
}
