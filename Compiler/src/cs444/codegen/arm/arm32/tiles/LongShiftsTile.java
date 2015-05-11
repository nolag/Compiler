package cs444.codegen.arm.arm32.tiles;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.RegisterShift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.B;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Orr;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Rsb;
import cs444.codegen.arm.instructions.Sub;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

@SuppressWarnings("rawtypes")
public class LongShiftsTile<T extends BinOpExpr> extends LongOnlyTile<ArmInstruction, Size, T> {

    private static final Map<Class<? extends BinOpExpr>, LongShiftsTile> tiles = new HashMap<>();

    private final Shift firstShift;
    private final Shift secondShift;
    private final Shift thirdShift;
    private final boolean zeroFirst;

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongShiftsTile<T> getTile(final Shift firstShift, final Shift secondShift, final Shift thirdShift,
            boolean zeroFirst, Class<T> klass) {
        LongShiftsTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new LongShiftsTile(firstShift, secondShift, thirdShift, zeroFirst);
            tiles.put(klass, tile);
        }
        return tile;
    }

    private LongShiftsTile(final Shift firstShift, final Shift secondShift, final Shift thirdShift, final boolean zeroFirst) {
        this.firstShift = firstShift;
        this.secondShift = secondShift;
        this.thirdShift = thirdShift;
        this.zeroFirst = zeroFirst;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();
        long mynum = CodeGenVisitor.getNewLblNum();

        instructions.add(platform.makeComment("Start long shift"));

        final Typeable t1 = (Typeable) bin.children.get(0);
        final Typeable t2 = (Typeable) bin.children.get(1);

        instructions.addAll(platform.getBest(t1));
        tileHelper.makeLong(t1, instructions, sizeHelper);
        tileHelper.pushLong(t1, instructions, sizeHelper);

        final Register first;
        final Register second;
        if (zeroFirst) {
            first = Register.R0;
            second = Register.R2;
        } else {
            first = Register.R2;
            second = Register.R0;
        }

        instructions.addAll(platform.getBest(t2));
        instructions.add(new Mov(Register.R1, Register.R0, sizeHelper));

        final String shiftEnd = "shiftEnd" + mynum;

        instructions.add(new Pop(Register.R0, Register.R2));
        instructions.add(new Sub(true, Register.R3, Register.R1, Immediate8.SIXTY_FOUR, sizeHelper));
        // >= 64
        instructions.add(new Eor(Condition.PL, Register.R0, Register.R0, Register.R0, sizeHelper));
        instructions.add(new Eor(Condition.PL, Register.R2, Register.R2, Register.R2, sizeHelper));
        instructions.add(new B(Condition.PL, shiftEnd));

        instructions.add(new Rsb(true, Register.R3, Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        // >= 32
        instructions.add(new Sub(Condition.MI, Register.R3, Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new Mov(Condition.MI, first, new RegisterShift(second, Register.R3, thirdShift), sizeHelper));
        instructions.add(new Eor(Condition.MI, second, second, second, sizeHelper));
        // < 32
        instructions.add(new Mov(Condition.PL, first, new RegisterShift(first, Register.R1, firstShift), sizeHelper));
        instructions.add(new Orr(Condition.PL, first, first, new RegisterShift(second, Register.R3, secondShift), sizeHelper));
        instructions.add(new Mov(Condition.PL, second, new RegisterShift(second, Register.R1, thirdShift), sizeHelper));

        instructions.add(platform.makeLabel(shiftEnd));
        instructions.add(platform.makeComment("End long shift"));
        return instructions;
    }
}