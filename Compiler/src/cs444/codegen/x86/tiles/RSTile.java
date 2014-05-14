package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.RSOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class RSTile extends SizedBinOpTile<RSExprSymbol> {
    private static RSTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if (tile == null) tile = new RSTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).rss.add(tile);
    }

    private RSTile() {
        super(RSOpMaker.maker, true);
    }
}
