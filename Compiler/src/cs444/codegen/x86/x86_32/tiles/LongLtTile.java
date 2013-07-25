package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JbMaker;
import cs444.codegen.x86.instructions.factories.JgMaker;
import cs444.codegen.x86.instructions.factories.JlMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LongLtTile extends LongJxxTile<LtExprSymbol>{
    public static void init(){
        new LongLtTile();
    }

    private LongLtTile(){
        super(JlMaker.maker, JgMaker.maker, JbMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).lts.add(this);
    }
}
