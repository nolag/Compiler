package cs444.codegen.arm.arm32.tiles.helpers;

import cs444.codegen.arm.Size;
import cs444.codegen.arm.arm32.Arm32Platform;
import cs444.codegen.arm.arm32.tiles.LongAssignmentTile;
import cs444.codegen.arm.arm32.tiles.LongDclTile;
import cs444.codegen.arm.arm32.tiles.StaticNameValue;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileInit;
import cs444.codegen.tiles.TileSet;

public class Arm32TileInit extends ArmTileInit {
    public static final Arm32TileInit instance = new Arm32TileInit();

    private Arm32TileInit() {
        super(Arm32Platform.class);
    }

    @Override
    public void initBase() {
        super.initBase();
        final TileSet<ArmInstruction, Size> set = TileSet.getOrMake(Arm32Platform.class);
        /*set.casts.add(CastFromLongTile.getTile());
        set.adds.add(LongBinTile.<AddExprSymbol> getTile(AddOpMaker.maker, AdcOpMaker.maker, false, AddExprSymbol.class));
        set.arrayValues.add(LongArrayValueTile.getTile());*/
        set.assigns.add(LongAssignmentTile.getTile());
        set.dcls.add(LongDclTile.getTile());
        /*set.divs.add(LongDivTile.getTile());
        set.eqs.add(LongJxxTile.<EqExprSymbol> getTile(null, JneMaker.maker, JeMaker.maker, EqExprSymbol.class));
        set.ges.add(LongJxxTile.<GeExprSymbol> getTile(JlMaker.maker, JgMaker.maker, JleMaker.maker, GeExprSymbol.class));
        set.gts.add(LongJxxTile.<GtExprSymbol> getTile(JlMaker.maker, JgMaker.maker, JaMaker.maker, GtExprSymbol.class));
        set.lss.add(LongLeftShift.getTile());
        set.les.add(LongJxxTile.<LeExprSymbol> getTile(JgMaker.maker, JlMaker.maker, JgeMaker.maker, LeExprSymbol.class));
        set.lts.add(LongJxxTile.<LtExprSymbol> getTile(JgMaker.maker, JlMaker.maker, JaMaker.maker, LtExprSymbol.class));
        set.mults.add(LongMultTile.getTile());
        set.nameValues.add(LongNameValueTile.getTile());
        set.negs.add(LongNegTile.getTile());
        set.nes.add(LongJxxTile.<NeExprSymbol> getTile(JneMaker.maker, null, JneMaker.maker, NeExprSymbol.class));
        set.rems.add(LongRemTile.getTile());
        set.rss.add(LongRigthShiftTile.getTile());
        set.subs.add(LongBinTile.<SubtractExprSymbol> getTile(SubOpMaker.maker, SbbOpMaker.maker, true, SubtractExprSymbol.class));
        set.urss.add(LongUnsignedRightShift.getTile());*/
        set.nameValues.add(StaticNameValue.getTile());
    }

    //TODO optomizations
}
