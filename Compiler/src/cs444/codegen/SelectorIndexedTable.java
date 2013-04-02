package cs444.codegen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.InstructionArg.Size;
import cs444.types.APkgClassResolver;

public class SelectorIndexedTable {
    private IndexedTableData table = new IndexedTableData(SizeHelper.getIntSize(Size.DWORD));

    public void addIndex(String className, String selector, String methodImplLabel) {
        table.addIndex(className, selector, methodImplLabel);
    }

    public Map<String, String> addClass(String fullName) {
        return table.addColumn(fullName);
    }

    public void addSelector(String selector) {
        table.addRow(selector);
    }

    public int getOffset(String selector){
        return table.getOffset(selector);
    }

    public static SelectorIndexedTable generateSIT(List<APkgClassResolver> resolvers, boolean outputFile, String directory)
                    throws IOException, FileNotFoundException {

        SelectorIndexedTable sit = new SelectorIndexedTable();

        for(APkgClassResolver resolver : resolvers){
            resolver.addToSelectorIndexedTable(sit);
        }

        sit.table.genCode();

        if(outputFile){
            sit.table.printCodeToFile(directory + "_joos.sit.s");
        }

        return sit;
    }
}
