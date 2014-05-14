package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;

public class EOrTile extends BinOpTile<EOrExprSymbol> {
    protected EOrTile() {
        super(AddOpMaker.maker);
    }

    private static EOrTile tile;
    
    public static void init(final Class<? extends Platform<ArmInstruction, Size>> klass){
        if(tile == null) tile = new EOrTile();
        TileSet.<ArmInstruction, Size>getOrMake(klass).eors.add(tile);
    }

    @Override
    public boolean fits(EOrExprSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return true;
    }
}
