package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class AddTile extends SizedBinOpTile<AddExprSymbol> {
    private static AddTile tile;

    public static AddTile getTile() {
        if (tile == null) tile = new AddTile();
        return tile;
    }

    public AddTile() {
        super(AddOpMaker.maker);
    }

    @Override
    public boolean fits(final AddExprSymbol op, final Platform<ArmInstruction, Size> platform) {
        return super.fits(op, platform) && !op.getType().value.equals(JoosNonTerminal.STRING);
    }
}