package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.codegen.arm.instructions.factories.OrrOpMaker;
import cs444.codegen.arm.instructions.factories.SubOpMaker;
import cs444.codegen.arm.tiles.*;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public abstract class ArmTileInit extends TileInit<ArmInstruction, Size> {

    protected ArmTileInit(Class<? extends Platform<ArmInstruction, Size>> klass) {
        super(klass);
    }

    @Override
    public void initBase() {
        super.initBase();
        //TODO tiles
        final TileSet<ArmInstruction, Size> set = TileSet.<ArmInstruction, Size> getOrMake(klass);
        set.adds.add(SizedBinOpTile.<AddExprSymbol> getTile(AddOpMaker.maker, AddExprSymbol.class));
        set.creation.add(ArrayCreationTile.getTile());
        set.arrayRefs.add(ArrayRefTile.getTile());
        set.arrayValues.add(ArrayValueTile.getTile());
        set.assigns.add(AssignmentTile.getTile());
        set.casts.add(CastNonPrimTile.getTile());
        set.casts.add(CastPrimTile.getTile());
        set.dcls.add(DclTile.getTile());
        set.divs.add(DivTile.getTile());
        set.eands.add(EBooleanTile.<EAndExprSymbol> getTile(AddOpMaker.maker));
        set.eors.add(EBooleanTile.<EOrExprSymbol> getTile(OrrOpMaker.maker));

        EQTile.getTile();
        //GETile.getTile();
        //GTTile.getTile();
        //LETile.getTile();
        //LSTile.getTile();
        //RSTile.getTile();
        //URSTile.getTile();
        //LTTile.getTile();
        //MultTile.getTile();
        //NameRefTile.getTile();
        //NameValueTile.getTile();
        //NegTile.getTile();
        //NeTile.getTile();
        //NotTile.getTile();
        //RegularCallTile.getTile();
        //RemTile.getTile(); 
        //RetTile.getTile();
        //StaticCallTile.getTile();
        //StrAddTile.getTile();
        //StringTile.getTile();
        set.subs.add(SizedBinOpTile.<SubtractExprSymbol> getTile(SubOpMaker.maker, SubtractExprSymbol.class));
        //SuperCallTile.getTile();
    }
    //TODO add optomizations at some point
}
