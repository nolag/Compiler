package cs444.codegen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Dd;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Instruction;
import cs444.types.APkgClassResolver;

public class SelectorIndexedTable {
    private IndexedTableData table = new IndexedTableData(new SelectorCellGen(), SizeHelper.getIntSize(Size.DWORD));

    public void addIndex(String className, String selector, String methodImplLabel) {
        table.addIndex(className, selector, methodImplLabel);
    }

    public Map<String, String> addClass(String fullName) {
        return table.addColumn(fullName);
    }

    public void addSelector(String selector) {
        table.addRow(selector);
    }

    public long getOffset(String selector){
        return table.getOffset(selector);
    }

    public static SelectorIndexedTable generateSIT(List<APkgClassResolver> resolvers, boolean outputFile, String directory)
                    throws IOException, FileNotFoundException {

        SelectorIndexedTable sit = new SelectorIndexedTable();

        for(APkgClassResolver resolver : resolvers){
            if(resolver.shouldGenCode()) resolver.addToSelectorIndexedTable(sit);
        }

        sit.table.genCode();

        if(outputFile){
            sit.table.printCodeToFile(directory + "_joos.sit.s");
        }

        return sit;
    }

    public class SelectorCellGen implements ICellCodeGenerator {

        @Override
        public void genEmptyCelCode(String colHeaderLabel, String rowName, List<Instruction> instructions) {
            instructions.add(new Comment(colHeaderLabel + " does not have access to " + rowName + ":"));
            instructions.add(new Dd(Immediate.NULL));
        }

        @Override
        public void genCellCode(String colHeaderLabel, String rowName,
                String data, List<Instruction> instructions) {
            instructions.add(new Extern(data));
            instructions.add(new Dd(new Immediate(data)));
        }

    }
}
