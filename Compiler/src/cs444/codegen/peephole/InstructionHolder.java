package cs444.codegen.peephole;

import java.io.PrintStream;

import cs444.codegen.instructions.Instruction;

public interface InstructionHolder {
    void passToNext(PrintStream printer);
    void add(Instruction instruction);
}
