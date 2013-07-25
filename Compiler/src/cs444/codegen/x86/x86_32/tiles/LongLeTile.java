package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JbeMaker;
import cs444.codegen.x86.instructions.factories.JgMaker;
import cs444.codegen.x86.instructions.factories.JlMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;

public class LongLeTile extends LongJxxTile<LeExprSymbol>{
    public static void init(){
        new LongLeTile();
    }

    private LongLeTile(){
        super(JlMaker.maker, JgMaker.maker, JbeMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).les.add(this);
    }
}
