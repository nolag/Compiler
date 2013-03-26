package cs444.codegen;

public abstract class InstructionArg {
    public enum Size { LOW, HIGH, WORD, DWORD };

    public static String getSizeStr(Size size){
        switch (size){
        case LOW:
        case HIGH:
            return "byte";
        case WORD:
            return "word";
        case DWORD:
            return "dword";
        }

        return null;
    }

    public abstract String getValue(Size size);

    public String getValue(){
        return getValue(Size.DWORD);
    }
}
