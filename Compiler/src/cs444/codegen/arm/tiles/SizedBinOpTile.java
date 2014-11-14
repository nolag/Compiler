package cs444.codegen.arm.tiles;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpRegMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

@SuppressWarnings("rawtypes")
public class SizedBinOpTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<BinOpRegMaker, SizedBinOpTile> tiles = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> SizedBinOpTile<T> getTile(final BinOpRegMaker maker) {
        SizedBinOpTile<T> tile = tiles.get(maker);
        if (tile == null) {
            tile = new SizedBinOpTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    protected SizedBinOpTile(final BinOpRegMaker maker) {
        super(maker);
    }

    @Override
    public boolean fits(final T op, final Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }
}
