package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetleMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;

public class LETile extends CompOpTile<LeExprSymbol>{
    public static void init(){
        new LETile();
    }

    private LETile() {
        super(SetleMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).les.add(this);
    }
}
