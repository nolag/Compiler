package cs444.codegen.instructions.factories;

import cs444.codegen.Register;
import cs444.codegen.instructions.Instruction;

public interface BinOpMaker {
    public Instruction make(Register one, Register two);
}
