package cs444.codegen.x86.instructions;

import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class Section extends X86Instruction {
    public static Section DATA = new Section("data");
    public static Section BSS = new Section("bss");
    public static Section TEXT = new Section("text");

    private final String type;

    private Section(String type) {
        super(0, 0);
        this.type = type;
    }

    @Override
    public String generate() {
        return "section ." + type;
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        return false;
    }
}
