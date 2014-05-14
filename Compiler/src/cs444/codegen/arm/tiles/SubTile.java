package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.SubOpMaker;
import cs444.codegen.arm.tiles.helpers.SizedBinOpTile;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class SubTile extends SizedBinOpTile<SubtractExprSymbol> {
    protected SubTile() {
        super(SubOpMaker.maker);
    }

    private static SubTile tile;
    
    public static void init(final Class<? extends Platform<ArmInstruction, Size>> klass){
        if(tile == null) tile = new SubTile();
        TileSet.<ArmInstruction, Size>getOrMake(klass).subs.add(tile);
    }

    @Override
    public boolean fits(SubtractExprSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return true;
    }
}
