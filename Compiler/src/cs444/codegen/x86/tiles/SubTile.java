package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SubOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class SubTile extends SizedBinOpTile<SubtractExprSymbol> {
    public static void init(){
        new SubTile();
    }

    private SubTile(){
        super(SubOpMaker.maker, true);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).subs.add(this);
    }
}
