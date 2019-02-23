package cs444.codegen.x86;

public enum Size { LOW, HIGH, WORD, DWORD, QWORD;

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
} };