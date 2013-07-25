package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.LSOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.LSExprSymbol;

public class LSTile extends SizedBinOpTile<LSExprSymbol> {
    public static void init(){
        new LSTile();
    }

    private LSTile(){
        super(LSOpMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).lss.add(this);
    }
}
