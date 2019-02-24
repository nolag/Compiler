package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class EBooleanTile<T extends BinOpExpr> extends BinOpTile<T> {
    private static final Map<BinOpMaker, EBooleanTile> tiles = new HashMap<>();

    private EBooleanTile(BinOpMaker maker) {
        super(maker, false);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> EBooleanTile<T> getTile(BinOpMaker maker) {
        EBooleanTile<T> tile = tiles.get(maker);
        if (tile == null) {
            tile = new EBooleanTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    @Override
    public boolean fits(T op, Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
