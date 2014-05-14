package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SeteMaker;
import cs444.codegen.x86.tiles.helpers.CompWithConstTile;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;

public class EQWithConstTile extends CompWithConstTile<EqExprSymbol> {
    private static EQWithConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new EQWithConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).eqs.add(tile);
    }

    private EQWithConstTile() {
        super(SeteMaker.maker);
    }
}
