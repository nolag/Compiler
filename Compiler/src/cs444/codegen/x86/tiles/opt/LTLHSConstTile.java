package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetgMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LTLHSConstTile extends CompWithConstTile<LtExprSymbol>{
    private static LTLHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new LTLHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).lts.add(tile);
    }

    private LTLHSConstTile() {
        super(SetgMaker.maker, Side.LEFT);
    }
}
