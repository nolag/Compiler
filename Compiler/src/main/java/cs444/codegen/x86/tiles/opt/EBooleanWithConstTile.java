package cs444.codegen.x86.tiles.opt;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

@SuppressWarnings("rawtypes")
public class EBooleanWithConstTile<T extends BinOpExpr> extends BinWithConstTile<T> {
    private static final Map<BinOpMaker, EBooleanWithConstTile> tiles = new HashMap<>();

    protected EBooleanWithConstTile(final BinOpMaker maker) {
        super(maker);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> EBooleanWithConstTile<T> getTile(final BinOpMaker maker) {
        EBooleanWithConstTile tile = tiles.get(maker);
        if (tile == null) {
            tile = new EBooleanWithConstTile(maker);
            tiles.put(maker, tile);
        }
        return tile;
    }

    @Override
    public boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform) && op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}