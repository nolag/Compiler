package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetlMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.GtExprSymbol;

public class GTLHSConstTile extends CompWithConstTile<GtExprSymbol>{
    private static GTLHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new GTLHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).gts.add(tile);
    }

    private GTLHSConstTile() {
        super(SetlMaker.maker, Side.LEFT);
    }
}
