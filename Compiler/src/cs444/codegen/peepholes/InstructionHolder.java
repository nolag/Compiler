package cs444.codegen.peepholes;

import java.io.PrintStream;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;

public interface InstructionHolder <T extends Instruction> extends Addable<T>{
    void flush(Platform<T, ?> platform, PrintStream printer);
}
