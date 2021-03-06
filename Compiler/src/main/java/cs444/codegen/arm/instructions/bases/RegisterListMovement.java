package cs444.codegen.arm.instructions.bases;

import cs444.codegen.arm.Register;
import cs444.codegen.instructions.InstructionArg;

public abstract class RegisterListMovement extends ArmInstruction {
    private final String instruction;
    private final Register[] registers;

    protected RegisterListMovement(String instruction, Register... registers) {
        super(registers.length < 5 ? 2 : registers.length / 2 + registers.length % 2);
        this.instruction = instruction;
        this.registers = registers;
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(instruction).append(" {");
        for (Register register : registers) {
            sb.append(register + ", ");
        }
        return sb.delete(sb.length() - 2, sb.length()).append("}").toString();
    }

    @Override
    public final boolean uses(InstructionArg<ArmInstruction, ?> what) {
        for (Register register : registers) {
            if (register.uses(what)) {
                return true;
            }
        }
        return false;
    }
}
