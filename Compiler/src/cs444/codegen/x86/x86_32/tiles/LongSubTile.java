package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SbbOpMaker;
import cs444.codegen.x86.instructions.factories.SubOpMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongBinTile;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class LongSubTile extends LongBinTile<SubtractExprSymbol> {
    private static LongSubTile tile;

    public static void init() {
        if(tile == null) tile = new LongSubTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).subs.add(tile);
    }

    private LongSubTile() {
        super(SubOpMaker.maker, SbbOpMaker.maker, true);
    }
}