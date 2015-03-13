package cs444.codegen.arm.arm32.tiles;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.RegisterShift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Orr;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
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
public class LongLogialShifts<T extends BinOpExpr> extends LongOnlyTile<ArmInstruction, Size, T> {

    private static final Map<Class<? extends BinOpExpr>, LongLogialShifts> tiles = new HashMap<>();

    private final Shift primary;
    private final Shift small;
    private final boolean zeroFirst;

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongLogialShifts<T> getTile(final Shift primary, final Shift small, boolean zeroFirst,
            Class<T> klass) {
        LongLogialShifts<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new LongLogialShifts(primary, small, zeroFirst);
            tiles.put(klass, tile);
        }
        return tile;
    }

    private LongLogialShifts(final Shift primary, final Shift small, final boolean zeroFirst) {
        this.primary = primary;
        this.small = small;
        this.zeroFirst = zeroFirst;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long shift"));
        final Typeable lhs = (Typeable) bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.R2, Register.R0));

        final Register first;
        final Register second;

        if (zeroFirst) {
            first = Register.R0;
            second = Register.R2;
        } else {
            first = Register.R2;
            second = Register.R0;
        }

        final Typeable rhs = (Typeable) bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        instructions.add(new Mov(Register.R1, Register.R0, sizeHelper));
        instructions.add(new Pop(Register.R0, Register.R2));

        instructions.add(new Rsb(true, Register.R3, Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new Sub(Condition.MI, Register.R3, Register.R1, Immediate8.THIRTY_TWO, sizeHelper));
        instructions.add(new Mov(Condition.MI, first, new RegisterShift(second, Register.R3, primary), sizeHelper));
        instructions.add(new Mov(Condition.PL, first, new RegisterShift(first, Register.R1, primary), sizeHelper));
        instructions.add(new Orr(Condition.PL, first, first, new RegisterShift(second, Register.R3, small), sizeHelper));
        instructions.add(new Mov(second, new RegisterShift(second, first, primary), sizeHelper));

        instructions.add(new Comment("End long shift"));
        return instructions;
    }
}