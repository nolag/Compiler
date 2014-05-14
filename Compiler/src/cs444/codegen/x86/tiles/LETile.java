package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetgeMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;

public class LETile extends CompOpTile<LeExprSymbol>{
    private static LETile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new LETile();
        TileSet.<X86Instruction, Size>getOrMake(klass).les.add(tile);
    }

    private LETile() {
        super(SetgeMaker.maker, false);
    }
}
