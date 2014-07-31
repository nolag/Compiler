package cs444.codegen.arm;

public enum Size {
    B, SB, H, SH, W, D;
    public static String getSizeStr(Size size) {
        switch (size) {
        case B:
            return "B";
        case H:
            return "H";
        case SB:
            return "SB";
        case SH:
            return "SH";
        case D:
            return "D";
        case W:
            return "";
        }
        return null;
    }
}