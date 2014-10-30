package cs444.codegen.arm.tiles;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

@SuppressWarnings("rawtypes")
public class EBooleanTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<BinOpMaker, EBooleanTile> tiles = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> EBooleanTile<T> getTile(final BinOpMaker maker) {
        EBooleanTile<T> tile = tiles.get(maker);
        if (tile == null) {
            tile = new EBooleanTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    private EBooleanTile(BinOpMaker maker) {
        super(maker);
    }

    @Override
    public boolean fits(T op, Platform<ArmInstruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
