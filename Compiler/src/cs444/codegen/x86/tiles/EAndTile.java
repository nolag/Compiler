package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AndOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;

public class EAndTile extends BinOpTile<EAndExprSymbol> {
    private static EAndTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new EAndTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).eands.add(tile);
    }

    private EAndTile(){
        super(AndOpMaker.maker, false);
    }

    @Override
    public boolean fits(final EAndExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
