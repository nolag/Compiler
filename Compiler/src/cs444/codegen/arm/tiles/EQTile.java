package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.tiles.helpers.CompOpTile;

import cs444.parser.symbols.ast.expressions.EqExprSymbol;

public class EQTile extends CompOpTile<EqExprSymbol> {
    private static EQTile tile;

    public static EQTile getTile() {
        if (tile == null) tile = new EQTile();
        return tile;
    }

    private EQTile() {
        super(Condition.EQ, Condition.NE);
    }
}
