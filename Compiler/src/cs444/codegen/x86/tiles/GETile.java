package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetleMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.GeExprSymbol;

public class GETile extends CompOpTile<GeExprSymbol>{
    private static GETile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new GETile();
        TileSet.<X86Instruction, Size>getOrMake(klass).ges.add(tile);
    }

    private GETile() {
        super(SetleMaker.maker, false);
    }
}
