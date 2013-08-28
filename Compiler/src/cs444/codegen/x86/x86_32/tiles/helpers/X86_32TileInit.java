package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.x86.tiles.helpers.X86TileInit;
import cs444.codegen.x86.x86_32.tiles.*;

public class X86_32TileInit extends X86TileInit{
    public static final X86_32TileInit instance = new X86_32TileInit();

    private X86_32TileInit(){ }

    @Override
    public void initBase(){
        super.initBase();
        CastFromLongTile.init();
        LongAddTile.init();
        LongArrayValueTile.init();
        LongAssignmentTile.init();
        LongDclTile.init();
        LongDivTile.init();
        LongEqTile.init();
        LongLeftShift.init();
        LongLeTile.init();
        LongLtTile.init();
        LongMultTile.init();
        LongNameValueTile.init();
        LongNegTile.init();
        LongNeTile.init();
        LongNumberTile.init();
        LongRemTile.init();
        LongRigthShiftTile.init();
        LongSubTile.init();
        LongUnsignedRightShift.init();
    }
}
