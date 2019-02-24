package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class EBooleanWithConstTile<T extends BinOpExpr> extends BinWithConstTile<T> {
    private static final Map<BinOpMaker, EBooleanWithConstTile> tiles = new HashMap<>();

    protected EBooleanWithConstTile(BinOpMaker maker) {
        super(maker);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> EBooleanWithConstTile<T> getTile(BinOpMaker maker) {
        EBooleanWithConstTile tile = tiles.get(maker);
        if (tile == null) {
            tile = new EBooleanWithConstTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    @Override
    public boolean fits(T op, Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform) && op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}