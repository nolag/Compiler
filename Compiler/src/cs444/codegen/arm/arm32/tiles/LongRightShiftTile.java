package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.RegisterShift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class LongRightShiftTile extends LongOnlyTile<ArmInstruction, Size, RSExprSymbol> {

    private static LongRightShiftTile tile;

    public static LongRightShiftTile getTile() {
        if (tile == null) tile = new LongRightShiftTile();
        return tile;
    }

    private LongRightShiftTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(RSExprSymbol rss, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long right arithmetic shift"));
        final Typeable lhs = (Typeable) rss.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.R2, Register.R0));

        final Typeable rhs = (Typeable) rss.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        instructions.add(new Mov(Register.R1, Register.R0, sizeHelper));
        instructions.add(new Pop(Register.R0, Register.R2));
        final String end = "ASREnd" + CodeGenVisitor.getNewLblNum();

        instructions.add(new Comment("Need to compare to zero later anyways, may as well be done if it is"));
        instructions.add(new Cmp(Register.R1, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, end));
        instructions.add(new Cmp(Register.R2, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new Mov(Condition.LT, Register.R4, Immediate8.ONE, sizeHelper));
        instructions.add(new Mov(Condition.GE, Register.R4, Immediate8.ZERO, sizeHelper));

        instructions.add(new Rsb(true, Register.R3, Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new Sub(Condition.MI, Register.R3, Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new Mov(Condition.MI, Register.R0, new RegisterShift(Register.R2, Register.R3, Shift.LSR), sizeHelper));
        instructions.add(new Mov(Condition.PL, Register.R0, new RegisterShift(Register.R0, Register.R1, Shift.LSR), sizeHelper));
        instructions
                .add(new Orr(Condition.PL, Register.R0, Register.R0, new RegisterShift(Register.R2, Register.R1, Shift.LSL), sizeHelper));
        instructions.add(new Mov(Register.R2, new RegisterShift(Register.R2, Register.R1, Shift.ASR), sizeHelper));

        instructions.add(new Comment("get the left most bits in the right spot"));
        instructions.add(new Cmp(Register.R4, Immediate8.ZERO, sizeHelper));
        instructions.add(new B(Condition.EQ, end));
        instructions.add(new Cmp(Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new B(Condition.LE, end));
        instructions.add(new Sub(Register.R1, Register.R1, Immediate8.THIRTY_THREE, sizeHelper));
        instructions.add(new Mov(Register.R4, new ConstantShift(Register.R4, (byte) 31, Shift.LSL), sizeHelper));
        instructions.add(new Mov(Register.R4, new RegisterShift(Register.R4, Register.R1, Shift.ASR), sizeHelper));

        instructions.add(new Comment("End long shift"));
        instructions.add(new Label(end));
        return instructions;
    }
}