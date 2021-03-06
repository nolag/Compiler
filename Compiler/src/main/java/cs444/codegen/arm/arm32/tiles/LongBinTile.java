package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpRegMaker;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class LongBinTile<T extends BinOpExpr> extends LongOnlyTile<ArmInstruction, Size, T> {

    private static final Map<Class<? extends BinOpExpr>, LongBinTile> tiles = new HashMap<>();

    private final BinOpRegMaker first;
    private final BinOpRegMaker second;

    private LongBinTile(BinOpRegMaker first, BinOpRegMaker second) {
        this.first = first;
        this.second = second;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongBinTile<T> getTile(BinOpRegMaker first, BinOpRegMaker second,
                                                               Class<T> klass) {
        LongBinTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new LongBinTile(first, second);
            tiles.put(klass, tile);
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long add or sub"));
        Typeable lhs = (Typeable) bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.R2, Register.R0));

        Typeable rhs = (Typeable) bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        instructions.add(new Pop(Register.R1));
        instructions.add(first.make(true, Register.R0, Register.R1, Register.R0, sizeHelper));
        instructions.add(new Pop(Register.R1));
        instructions.add(second.make(true, Register.R2, Register.R1, Register.R2, sizeHelper));

        instructions.add(new Comment("End long bin op"));
        return instructions;
    }
}
