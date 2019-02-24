package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpRegMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class SizedBinOpTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<BinOpRegMaker, SizedBinOpTile> tiles = new HashMap<>();

    protected SizedBinOpTile(BinOpRegMaker maker) {
        super(maker);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> SizedBinOpTile<T> getTile(BinOpRegMaker maker) {
        SizedBinOpTile<T> tile = tiles.get(maker);
        if (tile == null) {
            tile = new SizedBinOpTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    @Override
    public boolean fits(T op, Platform<ArmInstruction, Size> platform) {
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }
}
