package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;

public interface ICellGen<T extends Instruction<T>, E extends Enum<E>> {
    void genEmptyCelCode(String colHeaderLabel, String rowName, InstructionHolder<T> instructions);

    void genCellCode(String colHeaderLabel, String rowName, String data, InstructionHolder<T> instructions);

    long getCellSize();
}
