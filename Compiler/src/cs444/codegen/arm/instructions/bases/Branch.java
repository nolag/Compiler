package cs444.codegen.arm.instructions.bases;

import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.instructions.InstructionArg;

public abstract class Branch extends ArmInstruction {
    public enum Condition {
        EQ, NE, HS, LO, MI, PL, VS, CS, HI, LS, GE, LT, GT, LE, AL {
            @Override
            public String toString() {
                return "";
            }
        };

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public final String instruction;
    public final ImmediateStr where;
    public final Condition c;

    protected Branch(String instruction, Condition c, ImmediateStr where) {
        super(0);
        this.instruction = instruction;
        this.where = where;
        this.c = c;
    }

    @Override
    public boolean uses(InstructionArg<ArmInstruction, ?> what) {
        return false;
    }

    @Override
    public String generate() {
        return instruction + " " + c.toString() + " " + where;
    }
}
