package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetgMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.GtExprSymbol;

public class GTRHSConstTile extends CompWithConstTile<GtExprSymbol>{
    private static GTRHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new GTRHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).gts.add(tile);
    }

    private GTRHSConstTile() {
        super(SetgMaker.maker, Side.RIGHT);
    }
}
