package cs444.codegen.instructions;

public class Section implements Instruction {
    public enum SectionType { DATA, BSS, TEXT }

    private final SectionType type;
    public Section(SectionType type){
        this.type = type;
    }

    @Override
    public String generate() {
        String kind = "";
        switch (type) {
        case DATA:
            kind = ".data";
            break;
        case BSS:
            kind = ".bss";
            break;
        case TEXT:
            kind = ".text";
        default:
            // shouldn't get here
            break;
        }
        return "section " + kind;
    }

}