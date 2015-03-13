package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class LongDivTile<T extends BinOpExpr> extends LongOnlyTile<ArmInstruction, Size, T> {
    public final boolean divide;
    public final boolean bothForNeg;
    private static LongDivTile<DivideExprSymbol> div = new LongDivTile<DivideExprSymbol>(true, true);
    private static LongDivTile<RemainderExprSymbol> rem = new LongDivTile<RemainderExprSymbol>(false, false);

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongDivTile<T> getTile(boolean divide, boolean bothForNeg) {
        if (divide & bothForNeg) return (LongDivTile<T>) div;
        if (!(divide | bothForNeg)) return (LongDivTile<T>) rem;
        throw new IllegalArgumentException("This combination does not seem legal!");
    }

    private LongDivTile(final boolean divide, final boolean bothForNeg) {
        this.divide = divide;
        this.bothForNeg = bothForNeg;
    }

    private void helper(BinOpExpr div, Platform<ArmInstruction, Size> platform, final InstructionsAndTiming<ArmInstruction> instructions,
            long mynum, boolean high) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        final Register divSol;
        final Register qpart;
        final Register dpart;
        if (high) {
            divSol = Register.R11;
            qpart = Register.R6;
            dpart = Register.R5;
        } else {
            divSol = Register.R3;
            qpart = Register.R1;
            dpart = Register.R10;
        }

        instructions.add(new Mov(Register.R7, Immediate8.ONE, sizeHelper));
        instructions.add(new Mov(Register.R7, new ConstantShift(Register.R7, (byte) 31, Shift.LSL), sizeHelper));

        instructions.add(platform.makeComment("Time for long division"));
        final String loopStart = "divideStart" + mynum + (high ? "high" : "low");
        final String loopEnd = "divideEnd" + mynum + (high ? "high" : "low");
        tileHelper.setupLbl(loopStart, instructions);
        instructions.add(new Cmp(Register.R7, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, loopEnd));
        instructions.add(new Mov(divSol, new ConstantShift(divSol, (byte) 1, Shift.LSL), sizeHelper));

        instructions.add(platform.makeComment("This is the actual long division part for a bit"));
        instructions.add(new And(Register.LINK, Register.R7, qpart, sizeHelper));
        instructions.add(new Cmp(Register.LINK, Immediate8.ZERO, sizeHelper));

        instructions.add(new Add(Condition.NE, divSol, divSol, Immediate8.ONE, sizeHelper));

        instructions.add(new Cmp(Register.R11, Register.R2, sizeHelper));
        instructions.add(new Cmp(Register.R3, Register.R0, sizeHelper, Condition.EQ));

        instructions.add(new Orr(Condition.GE, dpart, dpart, Register.R7, sizeHelper));
        instructions.add(new Sub(Condition.GE, Register.R3, Register.R3, Register.R0, sizeHelper));
        instructions.add(new Sbc(Condition.GE, Register.R11, Register.R11, Register.R2, sizeHelper));
        instructions.add(platform.makeComment("End of the actual long division part for a bit"));

        instructions.add(new Mov(Register.R7, new ConstantShift(Register.R7, (byte) 1, Shift.LSR), sizeHelper));
        tileHelper.setupJump(loopStart, sizeHelper, instructions);
        tileHelper.setupLbl(loopEnd, instructions);
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T div, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final Typeable t1 = (Typeable) div.children.get(0);
        final Typeable t2 = (Typeable) div.children.get(1);

        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();
        instructions.addAll(platform.getBest(t1));
        tileHelper.makeLong(t1, instructions, sizeHelper);
        // We need to push R2 after R0 so can't combine
        instructions.add(new Push(Register.R0));
        instructions.add(new Push(Register.R2));

        instructions.addAll(platform.getBest(t2));
        tileHelper.makeLong(t2, instructions, sizeHelper);

        instructions.add(new Orr(true, Register.R0, Register.R0, Register.R2, sizeHelper));
        long mynum = CodeGenVisitor.getNewLblNum();
        final String safeDiv = "safeDiv" + mynum;
        instructions.add(new B(Condition.NE, safeDiv));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(platform.makeLabel(safeDiv));

        instructions.add(new Pop(Register.R1, Register.R6));

        instructions.add(platform.makeComment("Starting long div or rem"));
        instructions.add(new Push(Register.R8, Register.R9, Register.R10, Register.R11, Register.LINK));
        instructions.add(platform.makeComment("we need to check for -max int on both numbers, so may as well do the = check first"));
        final String doneDiv = "doneDiv" + mynum;
        instructions.add(new Cmp(Register.R0, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R2, Register.R6, sizeHelper, Condition.EQ));
        instructions.add(new Mov(Condition.EQ, Register.R0, divide ? Immediate8.ONE : Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, doneDiv));

        instructions.addAll(ArmSizeHelper.putInReg(Register.R4, Integer.MIN_VALUE, sizeHelper));
        instructions.add(new Cmp(Register.R0, Register.R4, sizeHelper));
        instructions.add(new Cmp(Register.R2, Register.R4, sizeHelper, Condition.EQ));
        instructions.add(new Mov(Condition.EQ, Register.R0, divide ? Immediate8.ZERO : Register.R1, sizeHelper));
        instructions.add(new B(Condition.EQ, doneDiv));

        instructions.add(platform.makeComment("Check for -ve value and save in R4"));
        instructions.add(new Cmp(Register.R6, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsb(Condition.LT, Register.R1, Register.R1, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsc(Condition.LT, Register.R6, Register.R6, Immediate8.ZERO, sizeHelper));
        instructions.add(new Mov(Condition.LT, Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(new Mov(Condition.GE, Register.R4, Immediate8.FALSE, sizeHelper));

        instructions.add(new Cmp(Register.R2, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsb(Condition.LT, Register.R0, Register.R0, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsc(Condition.LT, Register.R2, Register.R2, Immediate8.ZERO, sizeHelper));
        if (bothForNeg) instructions.add(new Eor(Condition.LT, Register.R4, Register.R4, Immediate8.TRUE, sizeHelper));

        instructions.add(platform.makeComment("Zero out quotient and remainder"));
        instructions.add(new Eor(Register.R3, Register.R3, Register.R3, sizeHelper));
        instructions.add(new Eor(Register.R11, Register.R11, Register.R11, sizeHelper));

        instructions.add(new Eor(Register.R5, Register.R5, Register.R5, sizeHelper));
        instructions.add(new Eor(Register.R10, Register.R10, Register.R10, sizeHelper));

        helper(div, platform, instructions, mynum, true);
        helper(div, platform, instructions, mynum, false);

        tileHelper.setupComment("Long division end " + mynum, instructions);
        instructions.add(divide ? new Mov(Register.R0, Register.R5, sizeHelper) : new Mov(Register.R0, Register.R3, sizeHelper));
        instructions.add(divide ? new Mov(Register.R2, Register.R10, sizeHelper) : new Mov(Register.R2, Register.R11, sizeHelper));
        instructions.add(platform.makeComment("deal with -ve values"));
        instructions.add(new Cmp(Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(new Rsb(Condition.EQ, Register.R0, Register.R0, Immediate8.ZERO, sizeHelper));
        instructions.add(platform.makeLabel(doneDiv));
        instructions.add(new Pop(Register.R8, Register.R9, Register.R10, Register.R11, Register.LINK));

        return instructions;
    }
}