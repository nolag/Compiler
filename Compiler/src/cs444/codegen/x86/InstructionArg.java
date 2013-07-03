package cs444.codegen.x86;

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

    public abstract String getValue(final Size size, final X86SizeHelper sizeHelper);

    public final String getValue(final X86SizeHelper sizeHelper){
        return getValue(sizeHelper.defaultStack, sizeHelper);
    }
}
