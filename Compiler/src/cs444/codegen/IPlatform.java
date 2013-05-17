package cs444.codegen;

import cs444.codegen.instructions.Instruction;

public interface IPlatform<T extends Instruction> {
    public SizeHelper<T> getSizeHelper();
    public ObjectLayout<T> getObjectLayout();
    public SelectorIndexedTable<T> getSelectorIndex();
    public SubtypeIndexedTable<T> getSubtypeTable();
}
