package cs444.codegen.peepholes;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;

import java.io.PrintStream;

public interface InstructionHolder<T extends Instruction<T>> extends Addable<T> {
    void flush(Platform<T, ?> platform, PrintStream printer);
}
