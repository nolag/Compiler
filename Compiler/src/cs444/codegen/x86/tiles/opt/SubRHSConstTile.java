package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SubOpMaker;
import cs444.codegen.x86.tiles.helpers.BinWithConstTile;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class SubRHSConstTile extends BinWithConstTile<SubtractExprSymbol> {
    private static SubRHSConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new SubRHSConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).subs.add(tile);
    }

    private SubRHSConstTile() {
        super(SubOpMaker.maker, Side.RIGHT);
    }
}
