package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AdcOpMaker;
import cs444.codegen.x86.instructions.factories.AddOpMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongBinTile;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class LongAddTile extends LongBinTile<AddExprSymbol> {
    public static void init(){
        new LongAddTile();
    }

    private LongAddTile(){
        super(AddOpMaker.maker, AdcOpMaker.maker, false);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).adds.add(this);
    }
}