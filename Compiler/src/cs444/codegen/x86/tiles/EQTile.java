package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.SeteMaker;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;

public class EQTile extends CompOpTile<EqExprSymbol> {
    private static EQTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new EQTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).eqs.add(tile);
    }

    private EQTile() {
        super(SeteMaker.maker, false);
    }
}
