package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.URSOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.URSExprSymbol;

public class URSTile extends SizedBinOpTile<URSExprSymbol> {
    public static void init(){
        new URSTile();
    }

    private URSTile(){
        super(URSOpMaker.maker, true);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).urss.add(this);
    }
}
