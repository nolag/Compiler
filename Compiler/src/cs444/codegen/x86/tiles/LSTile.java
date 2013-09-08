package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.LSOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.LSExprSymbol;

public class LSTile extends SizedBinOpTile<LSExprSymbol> {
    private static LSTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new LSTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).lss.add(tile);
    }

    private LSTile(){
        super(LSOpMaker.maker, true);
    }
}
