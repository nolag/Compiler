package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetlMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LTTile extends CompOpTile<LtExprSymbol>{
    public static void init(){
        new LTTile();
    }

    private LTTile() {
        super(SetlMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).lts.add(this);
    }
}
