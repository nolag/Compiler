package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Instruction;

public interface ICellCodeGenerator {

    void genEmptyCelCode(String colHeaderLabel, String rowName,
            List<Instruction> instructions);

    void genCellCode(String colHeaderLabel, String rowName, String data,
            List<Instruction> instructions);

}
