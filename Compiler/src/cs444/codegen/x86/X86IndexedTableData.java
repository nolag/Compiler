package cs444.codegen.x86;

import java.util.Map;

import cs444.codegen.ISubtypeCellGen;
import cs444.codegen.IndexedTableData;
import cs444.codegen.x86.instructions.Global;
import cs444.codegen.x86.instructions.Label;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class X86IndexedTableData extends IndexedTableData<X86Instruction> {

    public X86IndexedTableData(final ISubtypeCellGen<X86Instruction> cellGen,
            final long cellDataSize) {

        super(cellGen, cellDataSize);
    }

    @Override
    public void genCode() {
        // having data section in different files confuses GDB :-/
        // instructions.add(new Section(SectionType.DATA));

        for (final String colHeaderLabel : indexedTable.keySet()) {
            instructions.add(new Global(colHeaderLabel));
            instructions.add(new Label(colHeaderLabel));

            final Map<String, String> column = indexedTable.get(colHeaderLabel);

            for (final String selector : offset.keySet()) {
                final String implLabel = column.get(selector);
                if (implLabel == null) cellGen.genEmptyCelCode(colHeaderLabel, selector, instructions);
                else cellGen.genCellCode(colHeaderLabel, selector, implLabel, instructions);
            }
        }
    }

}