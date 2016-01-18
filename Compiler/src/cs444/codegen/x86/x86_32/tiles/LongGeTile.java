package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JbeMaker;
import cs444.codegen.x86.instructions.factories.JgMaker;
import cs444.codegen.x86.instructions.factories.JlMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongJxxTile;
import cs444.parser.symbols.ast.expressions.GeExprSymbol;

public class LongGeTile extends LongJxxTile<GeExprSymbol> {
    private static LongGeTile tile;

    public static void init() {
        if (tile == null) tile = new LongGeTile();
        TileSet.<X86Instruction, Size> getOrMake(X86_32Platform.class).ges.add(tile);
    }

    private LongGeTile() {
        super(JlMaker.maker, JgMaker.maker, JbeMaker.maker);
    }
}
