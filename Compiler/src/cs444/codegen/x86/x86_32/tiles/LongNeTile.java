package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JneMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;

public class LongNeTile extends LongJxxTile<NeExprSymbol>{
    public static void init(){
        new LongNeTile();
    }

    private LongNeTile(){
        super(JneMaker.maker, null, JneMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).nes.add(this);
    }
}
