package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class SizedBinOpTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<Class<? extends BinOpExpr>, SizedBinOpTile> tiles = new HashMap<>();

    protected SizedBinOpTile(BinOpMaker maker, boolean ordered) {
        super(maker, ordered);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> SizedBinOpTile<T> getTile(BinOpMaker maker, boolean ordered,
                                                                  Class<T> klass) {
        SizedBinOpTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new SizedBinOpTile(maker, ordered);
            tiles.put(klass, tile);
        }
        return tile;
    }

    @Override
    public boolean fits(T op, Platform<X86Instruction, Size> platform) {
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }
}
