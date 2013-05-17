package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;

public interface ISubtypeCellGen<T extends Instruction> {
    void genEmptyCelCode(String colHeaderLabel, String rowName, InstructionHolder<T> instructions);
    void genCellCode(String colHeaderLabel, String rowName, String data, InstructionHolder<T> instructions);
}
