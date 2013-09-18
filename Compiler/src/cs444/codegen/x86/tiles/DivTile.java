package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.IDivMaker;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivTile extends BinUniOpTile<DivideExprSymbol> {
    private static DivTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new DivTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).divs.add(tile);
    }

    private DivTile() {
        super(IDivMaker.maker, true);
    }
}
