package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JeMaker;
import cs444.codegen.x86.instructions.factories.JneMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;

public class LongEqTile extends LongJxxTile<EqExprSymbol>{
    public static void init(){
        new LongEqTile();
    }

    private LongEqTile(){
        super(null, JneMaker.maker, JeMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).eqs.add(this);
    }
}
