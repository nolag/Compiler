package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.URSOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.URSExprSymbol;

public class URSTile extends SizedBinOpTile<URSExprSymbol> {
    private static URSTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new URSTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).urss.add(tile);
    }

    private URSTile() {
        super(URSOpMaker.maker, true);
    }
}
