package cs444.codegen.x86;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public abstract class InstructionArg {
    public enum Size { LOW, HIGH, WORD, DWORD, QWORD };

    public static String getSizeStr(final Size size){
        switch (size){
        case LOW:
        case HIGH:
            return "byte";
        case WORD:
            return "word";
        case DWORD:
            return "dword";
        case QWORD:
            return "qword";
        }

        return null;
    }

    public abstract String getValue(final Size size, final SizeHelper<X86Instruction, Size> sizeHelper);

    public final String getValue(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return getValue(sizeHelper.getDefaultSize(), sizeHelper);
    }

    public abstract boolean uses(InstructionArg what);
}
