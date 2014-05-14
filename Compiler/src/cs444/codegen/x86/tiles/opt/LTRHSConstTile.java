package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetlMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LTRHSConstTile extends CompWithConstTile<LtExprSymbol>{
    private static LTRHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new LTRHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).lts.add(tile);
    }

    private LTRHSConstTile() {
        super(SetlMaker.maker, Side.RIGHT);
    }
}
