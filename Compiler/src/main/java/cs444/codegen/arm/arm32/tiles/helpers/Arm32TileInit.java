package cs444.codegen.arm.arm32.tiles.helpers;

import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.arm32.Arm32Platform;
import cs444.codegen.arm.arm32.tiles.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.factories.AdcOpMaker;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.codegen.arm.instructions.factories.SbcOpMaker;
import cs444.codegen.arm.instructions.factories.SubOpMaker;
import cs444.codegen.arm.tiles.StaticNameValue;
import cs444.codegen.arm.tiles.helpers.ArmTileInit;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.*;

public class Arm32TileInit extends ArmTileInit {
    public static final Arm32TileInit instance = new Arm32TileInit();

    private Arm32TileInit() {
        super(Arm32Platform.class);
    }

    @Override
    public void initBase() {
        super.initBase();
        TileSet<ArmInstruction, Size> set = TileSet.getOrMake(Arm32Platform.class);
        set.casts.add(CastFromLongTile.getTile());
        set.adds.add(LongBinTile.getTile(AddOpMaker.maker, AdcOpMaker.maker, AddExprSymbol.class));
        set.arrayValues.add(LongArrayValueTile.getTile());
        set.assigns.add(LongAssignmentTile.getTile());
        set.dcls.add(LongDclTile.getTile());
        set.divs.add(LongDivTile.getTile(true, true));
        set.eqs.add(LongCompOpTile.getTile(null, Condition.NE, Condition.EQ, EqExprSymbol.class));
        set.ges.add(LongCompOpTile.getTile(Condition.GT, Condition.LT, Condition.HS, GeExprSymbol.class));
        set.gts.add(LongCompOpTile.getTile(Condition.GT, Condition.LT, Condition.HI, GtExprSymbol.class));
        set.lss.add(LongShiftsTile.getTile(Shift.LSL, Shift.LSR, Shift.LSL, false, LSExprSymbol.class));
        set.les.add(LongCompOpTile.getTile(Condition.LT, Condition.GT, Condition.LS, LeExprSymbol.class));
        set.lts.add(LongCompOpTile.getTile(Condition.LT, Condition.GT, Condition.LO, LtExprSymbol.class));
        set.mults.add(LongMultTile.getTile());
        set.nameValues.add(LongNameValueTile.getTile());
        set.negs.add(LongNegTile.getTile());
        set.nes.add(LongCompOpTile.getTile(Condition.NE, null, Condition.NE, NeExprSymbol.class));
        set.rems.add(LongDivTile.getTile(false, false));
        set.rss.add(LongSignedRightShift.getTile());
        set.subs.add(LongBinTile.getTile(SubOpMaker.maker, SbcOpMaker.maker, SubtractExprSymbol.class));
        set.urss.add(LongShiftsTile.getTile(Shift.LSR, Shift.LSL, Shift.LSR, true, URSExprSymbol.class));
        set.nameValues.add(StaticNameValue.getTile());
        set.nameValues.add(LongStaticNameValue.getTile());
    }
    //TODO optomizations
}
