package cs444.codegen.peephole;

import java.io.PrintStream;
import java.util.Collection;

import cs444.codegen.instructions.Instruction;

public interface InstructionHolder <T extends Instruction> {
    void flush(PrintStream printer);
    void add(T instruction);
    void addAll(Collection<T> instructions);
}
