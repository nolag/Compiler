package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JaMaker;
import cs444.codegen.x86.instructions.factories.JgMaker;
import cs444.codegen.x86.instructions.factories.JlMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LongLtTile extends LongJxxTile<LtExprSymbol>{
    private static LongLtTile tile;

    public static void init(){
        if(tile == null) tile = new LongLtTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).lts.add(tile);
    }

    private LongLtTile(){
        super(JgMaker.maker, JlMaker.maker, JaMaker.maker);
    }
}
