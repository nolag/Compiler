package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpRegMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class EBooleanTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<BinOpRegMaker, EBooleanTile> tiles = new HashMap<>();

    private EBooleanTile(BinOpRegMaker maker) {
        super(maker);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> EBooleanTile<T> getTile(BinOpRegMaker maker) {
        EBooleanTile<T> tile = tiles.get(maker);
        if (tile == null) {
            tile = new EBooleanTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    @Override
    public boolean fits(T op, Platform<ArmInstruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
