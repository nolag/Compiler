package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JeMaker;
import cs444.codegen.x86.instructions.factories.JneMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;

public class LongEqTile extends LongJxxTile<EqExprSymbol> {
    private static LongEqTile tile;

    public static void init() {
        if(tile == null) tile = new LongEqTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).eqs.add(tile);
    }

    private LongEqTile() {
        super(null, JneMaker.maker, JeMaker.maker);
    }
}
