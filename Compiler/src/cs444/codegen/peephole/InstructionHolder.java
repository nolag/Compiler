package cs444.codegen.peephole;

import java.io.PrintStream;

import cs444.codegen.Addable;
import cs444.codegen.instructions.Instruction;

public interface InstructionHolder <T extends Instruction> extends Addable<T>{
    void flush(PrintStream printer);
}