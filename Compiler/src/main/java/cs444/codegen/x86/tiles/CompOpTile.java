package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.CmpMaker;
import cs444.codegen.x86.instructions.factories.UniOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class CompOpTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<Class<? extends BinOpExpr>, CompOpTile> tiles = new HashMap<>();
    private final UniOpMaker uni;

    private CompOpTile(UniOpMaker uni, boolean ordered) {
        super(CmpMaker.maker, ordered);
        this.uni = uni;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> CompOpTile<T> getTile(UniOpMaker maker, boolean ordered,
                                                              Class<T> klass) {
        CompOpTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new CompOpTile(maker, ordered);
            tiles.put(klass, tile);
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(T bin, Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        X86TileHelper.addCompEnding(instructions, uni, platform);
        return instructions;
    }

    @Override
    public final boolean fits(T op, Platform<X86Instruction, Size> platform) {
        return platform.getTileHelper().fitsSizedCompare(op, platform);
    }
}
