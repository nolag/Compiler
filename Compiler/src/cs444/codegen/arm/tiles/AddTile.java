package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.codegen.arm.tiles.helpers.SizedBinOpTile;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class AddTile extends SizedBinOpTile<AddExprSymbol> {
    protected AddTile() {
        super(AddOpMaker.maker);
    }

    private static AddTile tile;
    
    public static void init(final Class<? extends Platform<ArmInstruction, Size>> klass){
        if(tile == null) tile = new AddTile();
        TileSet.<ArmInstruction, Size>getOrMake(klass).adds.add(tile);
    }

    @Override
    public boolean fits(AddExprSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return true;
    }
}
