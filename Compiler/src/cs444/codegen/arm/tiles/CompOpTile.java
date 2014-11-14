package cs444.codegen.arm.tiles;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate16;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Cmp;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

@SuppressWarnings("rawtypes")
public class CompOpTile<T extends BinOpExpr> implements ITile<ArmInstruction, Size, T> {
    private static final Map<Condition, CompOpTile> tiles = new HashMap<>();

    public final Condition tcond;

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> CompOpTile<T> getTile(final Condition tcond) {
        CompOpTile<T> tile = tiles.get(tcond);
        if (tile == null) {
            tile = new CompOpTile(tcond);
            tiles.put(tcond, tile);
        }
        return tile;
    }

    private CompOpTile(final Condition tcond) {
        this.tcond = tcond;
    }

    @Override
    public boolean fits(T symbol, Platform<ArmInstruction, Size> platform) {
        return platform.getTileHelper().fitsSizedCompare(symbol, platform);
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        final Typeable t1 = (Typeable) bin.children.get(0);
        final Typeable t2 = (Typeable) bin.children.get(1);

        final TypeSymbol ts1 = t1.getType();
        final TypeSymbol ts2 = t2.getType();

        final boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.addAll(platform.getBest(t1));
        if (hasLong) platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        instructions.add(new Push(Register.R0));

        instructions.addAll(platform.getBest(t2));
        if (hasLong) platform.getTileHelper().makeLong(t2, instructions, sizeHelper);
        instructions.add(new Pop(Register.R1));

        instructions.add(new Cmp(Register.R1, Register.R0, sizeHelper));
        instructions.add(new Mov(tcond, Register.R0, (Immediate16) Immediate8.TRUE, sizeHelper));
        instructions.add(new Mov(tcond.getOpposite(), Register.R0, (Immediate16) Immediate8.FALSE, sizeHelper));

        return instructions;
    }
}
