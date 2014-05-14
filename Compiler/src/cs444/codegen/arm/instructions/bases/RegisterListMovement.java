package cs444.codegen.arm.instructions.bases;

import cs444.codegen.arm.Register;
import cs444.codegen.instructions.InstructionArg;

public abstract class RegisterListMovement extends ArmInstruction {
    private final String instruction;
    private final Register [] registers;
    
    protected RegisterListMovement(final String instruction, final Register ... registers) {
        super(registers.length < 5 ? 2 : registers.length / 2 + registers.length % 2);
        this.instruction = instruction;
        this.registers = registers;
    }
    
    
    @Override
    public String generate() {
        final StringBuilder sb = new StringBuilder(instruction);
        for (Register register : registers) sb.append(register.toString() + ",");
        return sb.deleteCharAt(sb.length()-1).toString();
    }
    
    @Override
    public final boolean uses(final InstructionArg<ArmInstruction, ?> what) {
        for (Register register : registers)  {
            if (register.uses(what)) return true;
        }
        return false;
    }
}
