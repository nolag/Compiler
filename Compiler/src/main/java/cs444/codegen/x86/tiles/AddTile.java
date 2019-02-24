package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AddOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class AddTile extends SizedBinOpTile<AddExprSymbol> {
    private static AddTile tile;

    public AddTile() {
        super(AddOpMaker.maker, false);
    }

    public static AddTile getTile() {
        if (tile == null) {
            tile = new AddTile();
        }
        return tile;
    }

    @Override
    public boolean fits(AddExprSymbol op, Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform) && !op.getType().value.equals(JoosNonTerminal.STRING);
    }
}