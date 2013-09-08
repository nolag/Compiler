package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetgMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LTTile extends CompOpTile<LtExprSymbol>{
    private static LTTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new LTTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).lts.add(tile);
    }

    private LTTile() {
        super(SetgMaker.maker, false);
    }
}
