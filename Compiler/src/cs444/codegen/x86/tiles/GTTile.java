package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetlMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.GtExprSymbol;

public class GTTile extends CompOpTile<GtExprSymbol>{
    private static GTTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new GTTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).gts.add(tile);
    }

    private GTTile() {
        super(SetlMaker.maker, false);
    }
}
