package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.AddTile;
import cs444.codegen.arm.tiles.ArrayCreationTile;
import cs444.codegen.arm.tiles.EAndTile;
import cs444.codegen.arm.tiles.EOrTile;

public abstract class ArmTileInit extends TileInit<ArmInstruction, Size> {

    protected ArmTileInit(Class<? extends Platform<ArmInstruction, Size>> klass) {
        super(klass);
    }

    @Override
    public void initBase() {
        super.initBase();
        //TODO tiles
        // /* = unlooked at // = looked at not ready/able to generic
        AddTile.init(klass);
        ArrayCreationTile.init(klass);
        //ArrayRefTile.init(klass);
        //ArrayValueTile.init(klass);
        //AssignmentTile.init(klass);
        //CastNonPrimTile.init(klass);
        //CastPrimTile.init(klass);
        //ConstructorTile.init(klass);
        //DclTile.init(klass);
        //DivTile.init(klass);
        EAndTile.init(klass);
        EOrTile.init(klass);
        //EQTile.init(klass);
        //GETile.init(klass);
        //GTTile.init(klass);
        //LETile.init(klass);
        //LSTile.init(klass);
        //RSTile.init(klass);
        //URSTile.init(klass);
        //LTTile.init(klass);
        //MultTile.init(klass);
        //NameRefTile.init(klass);
        //NameValueTile.init(klass);
        //NegTile.init(klass);
        //NeTile.init(klass);
        //NotTile.init(klass);
        //RegularCallTile.init(klass);
        //RemTile.init(klass); 
        //RetTile.init(klass);
        //StaticCallTile.init(klass);
        //StrAddTile.init(klass);
        //StringTile.init(klass);
        //SubTile.init(klass);
        //SuperCallTile.init(klass);
    }
    //TODO add optomizations at some point
}
