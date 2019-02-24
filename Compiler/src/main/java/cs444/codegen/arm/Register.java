package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Register extends SimpleInstructionArg implements Operand2 {
    public static final Register R0 = new Register(0);
    public static final Register R1 = new Register(1);
    public static final Register R2 = new Register(2);
    public static final Register R3 = new Register(3);
    public static final Register R4 = new Register(4);
    public static final Register R5 = new Register(5);
    public static final Register R6 = new Register(6);
    public static final Register R7 = new Register(7);
    public static final Register R8 = new Register(8);
    public static final Register R9 = new Register(9);
    public static final Register R10 = new Register(10);
    public static final Register R11 = new Register(11);

    public static final Register INTRA_PROCEDURE = new Register(12);
    public static final Register STACK = new Register(13);
    public static final Register LINK = new Register(14);
    public static final Register PC = new Register(15);

    public static final List<Register> specialRegisters = Collections.unmodifiableList(Arrays.asList(INTRA_PROCEDURE,
            STACK, LINK, PC));

    public final int number;

    protected Register(int number) {
        this.number = number;
    }

    @Override
    public String getValue(Size size, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return toString();
    }

    @Override
    public String toString() {
        return "%r" + number;
    }
}
