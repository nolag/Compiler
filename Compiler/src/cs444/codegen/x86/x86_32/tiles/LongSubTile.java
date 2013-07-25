package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SbbOpMaker;
import cs444.codegen.x86.instructions.factories.SubOpMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongBinTile;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class LongSubTile extends LongBinTile<SubtractExprSymbol> {
    public static void init(){
        new LongSubTile();
    }

    private LongSubTile(){
        super(SubOpMaker.maker, SbbOpMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).subs.add(this);
    }
}