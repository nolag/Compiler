package cs444.codegen.peephole;

import java.io.PrintStream;
import java.util.List;

import cs444.codegen.instructions.Instruction;

public interface InstructionHolder <T extends Instruction> {
    void passToNext(PrintStream printer);
    void passToNextClear(PrintStream printer);
    void add(T instruction);
    void addAll(List<T> instructions);
}
