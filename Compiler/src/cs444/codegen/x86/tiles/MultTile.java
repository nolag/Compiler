package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.IMulMaker;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class MultTile extends BinUniOpTile<MultiplyExprSymbol> {
    private static MultTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new MultTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).mults.add(tile);
    }

    private MultTile() {
        super(IMulMaker.maker, false);
    }
}
