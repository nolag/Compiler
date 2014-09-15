package cs444.codegen.arm.arm32.tiles.helpers;

import cs444.codegen.arm.arm32.Arm32Platform;
import cs444.codegen.arm.tiles.helpers.ArmTileInit;

public class Arm32TileInit extends ArmTileInit {
    public static final Arm32TileInit instance = new Arm32TileInit();

    private Arm32TileInit() {
        super(Arm32Platform.class);
    }

    @Override
    public void initBase() {
        super.initBase();
        /* TODO
        CastFromLongTile.init();
        LongAddTile.init();
        LongArrayValueTile.init();
        LongAssignmentTile.init();
        LongDclTile.init();
        LongDivTile.init();
        LongEqTile.init();
        LongGeTile.init();
        LongGtTile.init();
        LongLeftShift.init();
        LongLeTile.init();
        LongLtTile.init();
        LongMultTile.init();
        LongNameValueTile.init();
        LongNegTile.init();
        LongNeTile.init();
        LongRemTile.init();
        LongRigthShiftTile.init();
        LongSubTile.init();
        LongUnsignedRightShift.init();
        StaticNameValue.init();
         */
    }

    //TODO optomizations
}
