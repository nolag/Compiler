package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SubOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class SubTile extends SizedBinOpTile<SubtractExprSymbol> {
    private static SubTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new SubTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).subs.add(tile);
    }

    private SubTile() {
        super(SubOpMaker.maker, true);
    }
}
