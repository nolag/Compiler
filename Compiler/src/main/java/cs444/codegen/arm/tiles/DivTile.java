package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class DivTile<T extends BinOpExpr> extends NumericHelperTile<ArmInstruction, Size, T> {
    private static DivTile<DivideExprSymbol> div = new DivTile<DivideExprSymbol>(true, true);
    private static DivTile<RemainderExprSymbol> rem = new DivTile<RemainderExprSymbol>(false, false);
    public final boolean divide;
    public final boolean bothForNeg;

    private DivTile(boolean divide, boolean bothForNeg) {
        this.divide = divide;
        this.bothForNeg = bothForNeg;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> DivTile<T> getTile(boolean divide, boolean bothForNeg) {
        if (divide & bothForNeg) {
            return (DivTile<T>) div;
        }
        if (!(divide | bothForNeg)) {
            return (DivTile<T>) rem;
        }
        throw new IllegalArgumentException("This combination does not seem legal!");
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T div, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        Typeable t1 = (Typeable) div.children.get(0);
        Typeable t2 = (Typeable) div.children.get(1);

        TypeSymbol ts1 = t1.getType();
        TypeSymbol ts2 = t2.getType();

        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.addAll(platform.getBest(t1));
        if (hasLong) {
            tileHelper.makeLong(t1, instructions, sizeHelper);
        }
        instructions.add(new Push(Register.R0));

        instructions.addAll(platform.getBest(t2));
        if (hasLong) {
            tileHelper.makeLong(t2, instructions, sizeHelper);
        }

        instructions.add(new Cmp(Register.R0, Immediate8.ZERO, sizeHelper));
        long mynum = CodeGenVisitor.getNewLblNum();
        String safeDiv = "safeDiv" + mynum;
        instructions.add(new B(Condition.NE, safeDiv));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(platform.makeLabel(safeDiv));

        instructions.add(new Pop(Register.R1));

        instructions.add(platform.makeComment("Sdiv is not on most Arm processors :(, use long division"));
        // instructions.add(new Sdiv(Register.R0, Register.R1, Register.R0, platform.getSizeHelper()));

        instructions.add(platform.makeComment("we need to check for -max int on both numbers, so may as well do the =" +
                " check first"));
        String doneDiv = "doneDiv" + mynum;
        instructions.add(new Cmp(Register.R0, Register.R1, sizeHelper));
        instructions.add(new Mov(Condition.EQ, Register.R0, divide ? Immediate8.ONE : Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, doneDiv));
        instructions.addAll(ArmSizeHelper.putInReg(Register.R4, Integer.MIN_VALUE, sizeHelper));
        instructions.add(new Cmp(Register.R0, Register.R4, sizeHelper));
        instructions.add(new Mov(Condition.EQ, Register.R0, divide ? Immediate8.ZERO : Register.R1, sizeHelper));
        instructions.add(new B(Condition.EQ, doneDiv));

        instructions.add(platform.makeComment("Check for -ve value and save in R4"));
        instructions.add(new Cmp(Register.R1, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsb(Condition.LT, Register.R1, Register.R1, Immediate8.ZERO, sizeHelper));
        instructions.add(new Mov(Condition.LT, Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(new Mov(Condition.GE, Register.R4, Immediate8.FALSE, sizeHelper));

        instructions.add(new Cmp(Register.R0, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsb(Condition.LT, Register.R0, Register.R0, Immediate8.ZERO, sizeHelper));
        if (bothForNeg) {
            instructions.add(new Eor(Condition.LT, Register.R4, Register.R4, Immediate8.TRUE, sizeHelper));
        }

        char numbits = (char) (8 * sizeHelper.getByteSizeOfType(div) - 1);
        instructions.add(new Clz(Register.R3, Register.R1, sizeHelper));
        instructions.add(new Rsb(Register.R3, Register.R3, new Immediate8(numbits), sizeHelper));
        instructions.add(new Mov(Register.R2, Immediate8.ONE, sizeHelper));
        instructions.add(new Mov(Register.R2, new RegisterShift(Register.R2, Register.R3, Shift.LSL), sizeHelper));

        instructions.add(platform.makeComment("Zero out divide and remainder"));
        instructions.add(new Eor(Register.R3, Register.R3, Register.R3, sizeHelper));
        instructions.add(new Eor(Register.R5, Register.R5, Register.R5, sizeHelper));

        instructions.add(platform.makeComment("Time for long division"));
        String loopStart = "divideStart" + mynum;
        String loopEnd = "divideEnd" + mynum;
        tileHelper.setupLbl(loopStart, instructions);
        instructions.add(new Cmp(Register.R2, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, loopEnd));
        instructions.add(new Mov(Register.R3, new ConstantShift(Register.R3, (byte) 1, Shift.LSL), sizeHelper));

        instructions.add(platform.makeComment("This is the actual long division part for a bit"));
        instructions.add(new And(Register.R6, Register.R2, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R6, Immediate8.ZERO, sizeHelper));
        instructions.add(new Add(Condition.NE, Register.R3, Register.R3, Immediate8.ONE, sizeHelper));
        instructions.add(new Cmp(Register.R3, Register.R0, sizeHelper));
        instructions.add(new Orr(Condition.GE, Register.R5, Register.R5, Register.R2, sizeHelper));
        instructions.add(new Sub(Condition.GE, Register.R3, Register.R3, Register.R0, sizeHelper));
        instructions.add(platform.makeComment("End of the actual long division part for a bit"));

        instructions.add(new Mov(Register.R2, new ConstantShift(Register.R2, (byte) 1, Shift.LSR), sizeHelper));
        tileHelper.setupJump(loopStart, sizeHelper, instructions);
        tileHelper.setupLbl(loopEnd, instructions);
        tileHelper.setupComment("Long division (algorithm, not long type) end " + mynum, instructions);
        instructions.add(new Mov(Register.R0, divide ? Register.R5 : Register.R3, sizeHelper));

        instructions.add(platform.makeComment("deal with -ve values"));
        instructions.add(new Cmp(Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(new Rsb(Condition.EQ, Register.R0, Register.R0, Immediate8.ZERO, sizeHelper));
        instructions.add(platform.makeLabel(doneDiv));

        return instructions;
    }
}