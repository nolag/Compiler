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
        final TileSet<ArmInstruction, Size> set = TileSet.getOrMake(Arm32Platform.class);
        set.casts.add(CastFromLongTile.getTile());
        set.adds.add(LongBinTile.<AddExprSymbol> getTile(AddOpMaker.maker, AdcOpMaker.maker, AddExprSymbol.class));
        set.arrayValues.add(LongArrayValueTile.getTile());
        set.assigns.add(LongAssignmentTile.getTile());
        set.dcls.add(LongDclTile.getTile());
        set.divs.add(LongDivTile.<DivideExprSymbol> getTile(true, true));
        set.eqs.add(LongCompOpTile.<EqExprSymbol> getTile(null, Condition.NE, Condition.EQ, EqExprSymbol.class));
        set.ges.add(LongCompOpTile.<GeExprSymbol> getTile(Condition.GT, Condition.LT, Condition.GE, GeExprSymbol.class));
        set.gts.add(LongCompOpTile.<GtExprSymbol> getTile(Condition.GT, Condition.LT, Condition.GT, GtExprSymbol.class));
        set.lss.add(LongLogialShifts.getTile(Shift.LSL, Shift.LSR, false, LSExprSymbol.class));
        set.les.add(LongCompOpTile.<LeExprSymbol> getTile(Condition.LT, Condition.GT, Condition.LE, LeExprSymbol.class));
        set.lts.add(LongCompOpTile.<LtExprSymbol> getTile(Condition.LT, Condition.GT, Condition.LT, LtExprSymbol.class));
        set.mults.add(LongMultTile.getTile());
        set.nameValues.add(LongNameValueTile.getTile());
        set.negs.add(LongNegTile.getTile());
        set.nes.add(LongCompOpTile.<NeExprSymbol> getTile(Condition.NE, null, Condition.NE, NeExprSymbol.class));
        set.rems.add(LongDivTile.<RemainderExprSymbol> getTile(false, false));
        set.rss.add(LongRightShiftTile.getTile());
        set.subs.add(LongBinTile.<SubtractExprSymbol> getTile(SubOpMaker.maker, SbcOpMaker.maker, SubtractExprSymbol.class));
        set.urss.add(LongLogialShifts.getTile(Shift.LSR, Shift.LSL, true, URSExprSymbol.class));
        set.nameValues.add(StaticNameValue.getTile());
    }
    //TODO optomizations
}
