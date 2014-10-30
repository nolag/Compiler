package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AddOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class AddWithConstTile extends BinWithConstTile<AddExprSymbol> {
    private static AddWithConstTile tile;

    public static AddWithConstTile getTile() {
        if (tile == null) tile = new AddWithConstTile();
        return tile;
    }

    private AddWithConstTile() {
        super(AddOpMaker.maker);
    }

    @Override
    public boolean fits(final AddExprSymbol bin, final Platform<X86Instruction, Size> platform) {
        return super.fits(bin, platform) && !bin.getType().value.equals(JoosNonTerminal.STRING);
    }
}