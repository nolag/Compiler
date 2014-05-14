package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JneMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;

public class LongNeTile extends LongJxxTile<NeExprSymbol> {
    private static LongNeTile tile;

    public static void init() {
        if(tile == null) tile = new LongNeTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).nes.add(tile);
    }

    private LongNeTile() {
        super(JneMaker.maker, null, JneMaker.maker);
    }
}
