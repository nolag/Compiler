package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.RegisterShift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.arm32.tiles.helpers.Arm32TileHelper;
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

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T div, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final Typeable t1 = (Typeable) div.children.get(0);
        final Typeable t2 = (Typeable) div.children.get(1);

        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Push(Register.R8, Register.R9, Register.R10, Register.R11));

        instructions.addAll(platform.getBest(t1));
        tileHelper.makeLong(t1, instructions, sizeHelper);
        instructions.add(new Push(Register.R0, Register.R2));

        instructions.addAll(platform.getBest(t2));
        tileHelper.makeLong(t2, instructions, sizeHelper);

        instructions.add(new Cmp(Register.R2, Immediate8.ZERO, sizeHelper));
        instructions.add(new Cmp(Register.R0, Immediate8.ZERO, sizeHelper, Condition.EQ));
        long mynum = CodeGenVisitor.getNewLblNum();
        final String safeDiv = "safeDiv" + mynum;
        instructions.add(new B(Condition.NE, safeDiv));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(platform.makeLabel(safeDiv));

        instructions.add(new Pop(Register.R1, Register.R3));

        instructions.add(platform.makeComment("we need to check for -max int on both numbers, so may as well do the = check first"));
        final String doneDiv = "doneDiv" + mynum;
        instructions.add(new Cmp(Register.R0, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R2, Register.R3, sizeHelper, Condition.EQ));
        instructions.add(new Mov(Condition.EQ, Register.R0, divide ? Immediate8.ONE : Immediate8.ZERO, sizeHelper));
        instructions.add(new Mov(Condition.EQ, Register.R2, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, doneDiv));

        instructions.addAll(ArmSizeHelper.putInReg(Register.R5, Integer.MIN_VALUE, sizeHelper));
        instructions.addAll(ArmSizeHelper.putInReg(Register.R4, 0, sizeHelper));

        instructions.add(new Cmp(Register.R0, Register.R4, sizeHelper));
        instructions.add(new Cmp(Register.R2, Register.R5, sizeHelper, Condition.EQ));
        instructions.add(new Mov(Condition.EQ, Register.R0, divide ? Immediate8.ZERO : Register.R1, sizeHelper));
        instructions.add(new Mov(Condition.EQ, Register.R2, divide ? Immediate8.ZERO : Register.R3, sizeHelper));
        instructions.add(new B(Condition.EQ, doneDiv));

        String endNegation = "firstNegationEnd" + mynum;
        instructions.add(platform.makeComment("Check for -ve value and save in R4"));
        instructions.add(new Cmp(Register.R3, Immediate8.ZERO, sizeHelper));
        instructions.add(new Mov(Condition.GE, Register.R4, Immediate8.FALSE, sizeHelper));
        instructions.add(new B(Condition.GE, endNegation));
        Arm32TileHelper.negLog(Register.R1, Register.R3, instructions, sizeHelper);
        instructions.add(new Mov(Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(platform.makeLabel(endNegation));

        endNegation = "secondNegationEnd" + mynum;
        instructions.add(new Cmp(Register.R2, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.GE, endNegation));
        Arm32TileHelper.negLog(Register.R0, Register.R2, instructions, sizeHelper);
        if (bothForNeg) instructions.add(new Eor(Register.R4, Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(platform.makeLabel(endNegation));

        instructions.add(platform.makeComment("Zero out divide and remainder (11,7) (5,8)"));
        instructions.add(new Eor(Register.R11, Register.R11, Register.R11, sizeHelper));
        instructions.add(new Eor(Register.R7, Register.R7, Register.R7, sizeHelper));
        instructions.add(new Eor(Register.R5, Register.R5, Register.R5, sizeHelper));
        instructions.add(new Eor(Register.R8, Register.R8, Register.R8, sizeHelper));

        // Looking at the Div tile, R2 => R9
        String noFirst = "noFirstNumber" + mynum;
        instructions.add(new Clz(Register.R10, Register.R3, sizeHelper));
        instructions.add(new Cmp(Register.R10, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new B(Condition.EQ, noFirst));
        instructions.add(new Rsb(Register.R10, Register.R10, Immediate8.THIRTY_ONE, sizeHelper));
        instructions.add(new Mov(Register.R9, Immediate8.ONE, sizeHelper));
        instructions.add(new Mov(Register.R9, new RegisterShift(Register.R9, Register.R10, Shift.LSL), sizeHelper));

        instructions.add(platform.makeComment("Time for long division, first part"));
        String loopStart = "divideStartFirst" + mynum;
        String loopEnd = "divideEndFirst" + mynum;
        tileHelper.setupLbl(loopStart, instructions);
        instructions.add(new Cmp(Register.R9, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, loopEnd));
        instructions.add(platform.makeComment("No need to check the overflow, first set can't overflow"));
        instructions.add(new Mov(Register.R11, new ConstantShift(Register.R11, (byte) 1, Shift.LSL), sizeHelper));
        instructions.add(new Orr(Condition.VS, Register.R7, Register.R7, Immediate8.ONE, sizeHelper));
        instructions.add(platform.makeComment("This is the actual long division part for a bit"));
        instructions.add(new And(Register.R6, Register.R9, Register.R3, sizeHelper));
        instructions.add(new Cmp(Register.R6, Immediate8.ZERO, sizeHelper));
        instructions.add(new Add(Condition.NE, Register.R11, Register.R11, Immediate8.ONE, sizeHelper));

        String lt = "firstLess" + mynum;
        instructions.add(new Cmp(Register.R7, Register.R2, sizeHelper));
        instructions.add(new Orr(Condition.GT, Register.R8, Register.R8, Register.R9, sizeHelper));
        instructions.add(new Sub(Condition.GT, Register.R11, Register.R11, Register.R0, sizeHelper));
        instructions.add(new Sbc(Condition.GT, Register.R7, Register.R7, Register.R2, sizeHelper));
        instructions.add(new B(Condition.LT, lt));
        instructions.add(new Cmp(Register.R11, Register.R0, sizeHelper));
        instructions.add(new Orr(Condition.HS, Register.R8, Register.R8, Register.R9, sizeHelper));
        instructions.add(new Sub(Condition.HS, Register.R11, Register.R11, Register.R0, sizeHelper));
        instructions.add(new Sbc(Condition.HS, Register.R7, Register.R7, Register.R2, sizeHelper));
        instructions.add(platform.makeLabel(lt));

        instructions.add(platform.makeComment("End of the actual long division part for a bit"));
        instructions.add(new Mov(Register.R9, new ConstantShift(Register.R9, (byte) 1, Shift.LSR), sizeHelper));
        tileHelper.setupJump(loopStart, sizeHelper, instructions);
        tileHelper.setupLbl(loopEnd, instructions);
        tileHelper.setupComment("Long division (algorithm, not long type) end " + mynum, instructions);

        instructions.add(platform.makeLabel(noFirst));
        instructions.add(platform.makeComment("Time for long division, second part"));

        instructions.add(new Mov(Register.R10, Immediate8.THIRTY_ONE, sizeHelper));
        instructions.add(new Mov(Register.R9, Immediate8.ONE, sizeHelper));
        instructions.add(new Mov(Register.R9, new RegisterShift(Register.R9, Register.R10, Shift.LSL), sizeHelper));
        loopStart = "divideStartSecond" + mynum;
        loopEnd = "divideEndSecond" + mynum;
        tileHelper.setupLbl(loopStart, instructions);
        instructions.add(new Cmp(Register.R9, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, loopEnd));
        instructions.add(new Mov(true, Condition.AL, Register.R11, new ConstantShift(Register.R11, (byte) 1, Shift.LSL), sizeHelper));
        instructions.add(new Mov(Register.R7, new ConstantShift(Register.R7, (byte) 1, Shift.LSL), sizeHelper));
        instructions.add(new Orr(Condition.HS, Register.R7, Register.R7, Immediate8.ONE, sizeHelper));
        instructions.add(platform.makeComment("This is the actual long division part for a bit"));
        instructions.add(new And(Register.R6, Register.R9, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R6, Immediate8.ZERO, sizeHelper));
        instructions.add(new Add(Condition.NE, Register.R11, Register.R11, Immediate8.ONE, sizeHelper));

        lt = "secondLess" + mynum;
        instructions.add(new Cmp(Register.R7, Register.R2, sizeHelper));
        instructions.add(new Orr(Condition.GT, Register.R5, Register.R5, Register.R9, sizeHelper));
        instructions.add(new Sub(Condition.GT, Register.R11, Register.R11, Register.R0, sizeHelper));
        instructions.add(new Sbc(Condition.GT, Register.R7, Register.R7, Register.R2, sizeHelper));
        instructions.add(new B(Condition.LT, lt));
        instructions.add(new Cmp(Register.R11, Register.R0, sizeHelper));
        instructions.add(new Orr(Condition.HS, Register.R5, Register.R5, Register.R9, sizeHelper));
        instructions.add(new Sub(Condition.HS, Register.R11, Register.R11, Register.R0, sizeHelper));
        instructions.add(new Sbc(Condition.HS, Register.R7, Register.R7, Register.R2, sizeHelper));
        instructions.add(platform.makeLabel(lt));

        instructions.add(platform.makeComment("End of the actual long division part for a bit"));
        instructions.add(new Mov(Register.R9, new ConstantShift(Register.R9, (byte) 1, Shift.LSR), sizeHelper));
        tileHelper.setupJump(loopStart, sizeHelper, instructions);
        tileHelper.setupLbl(loopEnd, instructions);

        instructions.add(new Mov(Register.R0, divide ? Register.R5 : Register.R11, sizeHelper));
        instructions.add(new Mov(Register.R2, divide ? Register.R8 : Register.R7, sizeHelper));

        instructions.add(platform.makeComment("deal with -ve values"));
        instructions.add(new Cmp(Register.R4, Immediate8.TRUE, sizeHelper));
        instructions.add(new B(Condition.NE, doneDiv));
        instructions.add(new Rsb(true, Register.R0, Register.R0, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsc(Register.R2, Register.R2, Immediate8.ZERO, sizeHelper));
        instructions.add(platform.makeLabel(doneDiv));

        instructions.add(new Pop(Register.R8, Register.R9, Register.R10, Register.R11));
        return instructions;
    }
}