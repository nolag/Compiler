package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetleMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;

public class LERHSConstTile extends CompWithConstTile<LeExprSymbol>{
    private static LERHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new LERHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).les.add(tile);
    }

    private LERHSConstTile() {
        super(SetleMaker.maker, Side.RIGHT);
    }
}
