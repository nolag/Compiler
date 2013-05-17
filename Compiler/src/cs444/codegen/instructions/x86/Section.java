package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.X86Instruction;


public class Section implements X86Instruction {
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
