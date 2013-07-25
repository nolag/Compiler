package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.RSOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class RSTile extends SizedBinOpTile<RSExprSymbol> {
    public static void init(){
        new RSTile();
    }

    private RSTile(){
        super(RSOpMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).rss.add(this);
    }
}
