package cs444.codegen;

import cs444.codegen.instructions.Instruction;

public interface SizeHelper<T extends Instruction> {
    public abstract int getByteSizeOfType(String typeName);
    public abstract int getDefaultStackPower();
    public abstract int getDefaultStackSize();
    public int getMinSize();
}