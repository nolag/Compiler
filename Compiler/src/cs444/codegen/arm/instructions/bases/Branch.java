package cs444.codegen.arm.instructions.bases;

import cs444.codegen.instructions.InstructionArg;

public abstract class Branch extends ArmInstruction {
    public enum Condition {
        EQ, NE, HS, LO, MI, PL, VS, VC, HI, LS, GE, LT, GT, LE, AL {

            @Override
            public String toString() {
                return "";
            }
        };

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public Condition getOpposite() {
            switch (this) {
            case EQ:
                return NE;
            case GE:
                return LT;
            case GT:
                return LE;
            case HI:
                return LS;
            case HS:
                return LO;
            case LE:
                return GT;
            case LO:
                return HS;
            case LS:
                return HI;
            case LT:
                return GE;
            case MI:
                return PL;
            case NE:
                return EQ;
            case PL:
                return MI;
            case VS:
                return VC;
            case VC:
                return VS;
            default:
                return null;
            }
        }
    }

    public final String instruction;
    public final String where;
    public final Condition c;

    protected Branch(String instruction, Condition c, String where) {
        super(3);
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
