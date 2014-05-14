package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetneMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;

public class NeTile extends CompOpTile<NeExprSymbol>{
    private static NeTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new NeTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).nes.add(tile);
    }

    private NeTile() {
        super(SetneMaker.maker, false);
    }
}
