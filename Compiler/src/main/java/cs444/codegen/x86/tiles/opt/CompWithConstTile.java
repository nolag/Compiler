package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.CmpMaker;
import cs444.codegen.x86.instructions.factories.UniOpMaker;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class CompWithConstTile<T extends BinOpExpr> extends BinWithConstTile<T> {
    private static final Map<Side, Map<Class<? extends BinOpExpr>, CompWithConstTile>> tiles =
            new EnumMap<>(Side.class);

    private final UniOpMaker uni;

    private CompWithConstTile(UniOpMaker uni) {
        super(CmpMaker.maker);
        this.uni = uni;
    }

    private CompWithConstTile(UniOpMaker uni, Side side) {
        super(CmpMaker.maker, side);
        this.uni = uni;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> CompWithConstTile<T> getTile(UniOpMaker maker, Side side,
                                                                     Class<T> klass) {
        Map<Class<? extends BinOpExpr>, CompWithConstTile> binMap = tiles.get(side);
        if (binMap == null) {
            tiles.put(side, binMap = new HashMap<>());
        }
        CompWithConstTile tile = binMap.get(klass);
        if (tile == null) {
            tile = new CompWithConstTile(maker, side);
            binMap.put(klass, tile);
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
        return super.fits(op, platform) && platform.getTileHelper().fitsSizedCompare(op, platform);
    }
}
