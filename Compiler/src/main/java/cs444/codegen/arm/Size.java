package cs444.codegen.arm;

public enum Size {
    B, SB, H, SH, W, D;

    @Override
    public String toString() {
        switch (this) {
            case B:
                return "b";
            case H:
                return "h";
            case SB:
                return "sb";
            case SH:
                return "sh";
            case D:
                return "d";
            case W:
                return "";
        }
        // Can't get here make find bugs shut up about it
        return "<INVALID>";
    }
}