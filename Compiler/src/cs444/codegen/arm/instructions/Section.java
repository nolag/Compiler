package cs444.codegen.arm.instructions;

import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.instructions.InstructionArg;

public class Section extends ArmInstruction {
    public static Section DATA = new Section("data");
    public static Section BSS = new Section("bss");
    public static Section TEXT = new Section("text");
    
    private final String type;
    
    private Section(final String type){
        super(0, 0);
        this.type = type;
    }

    @Override
    public String generate() {
        return "section ." + type;
    }

    @Override
    public boolean uses(final InstructionArg<ArmInstruction, ?> what) {
        return false;
    }
}
