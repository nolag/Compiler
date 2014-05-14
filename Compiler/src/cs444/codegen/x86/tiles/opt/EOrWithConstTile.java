package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.OrOpMaker;
import cs444.codegen.x86.tiles.helpers.BinWithConstTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;

public class EOrWithConstTile extends BinWithConstTile<EOrExprSymbol> {
    private static EOrWithConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new EOrWithConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).eors.add(tile);
    }

    private EOrWithConstTile() {
        super(OrOpMaker.maker);
    }

    @Override
    public boolean fits(final EOrExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform)  && op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
