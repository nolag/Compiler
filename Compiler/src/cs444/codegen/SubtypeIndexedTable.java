package cs444.codegen;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Db;
import cs444.codegen.instructions.Instruction;
import cs444.types.APkgClassResolver;

public class SubtypeIndexedTable {
    public class SubtypeCellGen implements ICellCodeGenerator {

        @Override
        public void genEmptyCelCode(String colHeaderLabel, String rowName,
                List<Instruction> instructions) {
            instructions.add(new Comment(colHeaderLabel + " is not subtype of " + rowName));
            instructions.add(new Db(Immediate.FALSE));
        }

        @Override
        public void genCellCode(String colHeaderLabel, String rowName,
                String data, List<Instruction> instructions) {
            instructions.add(new Comment(colHeaderLabel + " is subtype of " + rowName));
            instructions.add(new Db(Immediate.TRUE));
        }

    }

    private IndexedTableData table = new IndexedTableData(new SubtypeCellGen(), SizeHelper.getIntSize(Size.LOW));

    public Map<String, String> addSubtype(String fullName) {
        return table.addColumn(fullName);
    }

    public void addSuperType(String className) {
        table.addRow(className);
    }

    public void addIndex(String subtypeITLbl, String superType) {
        table.addIndex(subtypeITLbl, superType);
    }

    public static SubtypeIndexedTable generateTable(
            List<APkgClassResolver> resolvers, boolean outputFile,
            String directory) throws IOException {

        SubtypeIndexedTable sit = new SubtypeIndexedTable();

        for(APkgClassResolver resolver : resolvers){
            resolver.addToSubtypeIndexedTable(sit);
        }

        sit.table.genCode();

        if(outputFile){
            sit.table.printCodeToFile(directory + "_joos.subtypeIT.s");
        }

        return sit;
    }
}
