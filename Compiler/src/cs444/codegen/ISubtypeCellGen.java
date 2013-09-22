package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;

public interface ISubtypeCellGen<T extends Instruction, E extends Enum<E>> {
    void genEmptyCelCode(String colHeaderLabel, String rowName, InstructionHolder<T> instructions, SizeHelper<T, E> sizeHelper);
    void genCellCode(String colHeaderLabel, String rowName, String data, InstructionHolder<T> instructions, SizeHelper<T, E> sizeHelper);
}
