package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;

public class EAndTile extends BinOpTile<EAndExprSymbol> {
    protected EAndTile() {
        super(AddOpMaker.maker);
    }

    private static EAndTile tile;
    
    public static void init(final Class<? extends Platform<ArmInstruction, Size>> klass){
        if(tile == null) tile = new EAndTile();
        TileSet.<ArmInstruction, Size>getOrMake(klass).eands.add(tile);
    }

    @Override
    public boolean fits(EAndExprSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return true;
    }
}
