package cs444.codegen.x86.tiles;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

@SuppressWarnings("rawtypes")
public class SizedBinOpTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<Class<? extends BinOpExpr>, SizedBinOpTile> tiles = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> SizedBinOpTile<T> getTile(final BinOpMaker maker, final boolean ordered, Class<T> klass) {
        SizedBinOpTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new SizedBinOpTile(maker, ordered);
            tiles.put(klass, tile);
        }
        return tile;
    }

    protected SizedBinOpTile(final BinOpMaker maker, final boolean ordered) {
        super(maker, ordered);
    }

    @Override
    public boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }
}
