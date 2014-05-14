package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AndOpMaker;
import cs444.codegen.x86.tiles.helpers.BinWithConstTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;

public class EAndWithConstTile extends BinWithConstTile<EAndExprSymbol> {
    private static EAndWithConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new EAndWithConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).eands.add(tile);
    }

    private EAndWithConstTile(){
        super(AndOpMaker.maker);
    }

    @Override
    public boolean fits(final EAndExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform)  && op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
