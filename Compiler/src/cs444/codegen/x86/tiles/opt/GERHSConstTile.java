package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SetgeMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.GeExprSymbol;

public class GERHSConstTile extends CompWithConstTile<GeExprSymbol>{
    private static GERHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new GERHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).ges.add(tile);
    }

    private GERHSConstTile() {
        super(SetgeMaker.maker, Side.RIGHT);
    }
}
