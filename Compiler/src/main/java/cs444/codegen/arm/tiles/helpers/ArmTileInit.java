package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.factories.AndOpMaker;
import cs444.codegen.arm.instructions.factories.MulOpMaker;
import cs444.codegen.arm.instructions.factories.OrrOpMaker;
import cs444.codegen.arm.instructions.factories.SubOpMaker;
import cs444.codegen.arm.tiles.*;
import cs444.codegen.tiles.TileSet;

public abstract class ArmTileInit extends TileInit<ArmInstruction, Size> {

    protected ArmTileInit(Class<? extends Platform<ArmInstruction, Size>> klass) {
        super(klass);
    }

    @Override
    public void initBase() {
        super.initBase();
        TileSet<ArmInstruction, Size> set = TileSet.getOrMake(klass);
        set.adds.add(AddTile.getTile());
        set.creation.add(ArrayCreationTile.getTile());
        set.arrayRefs.add(ArrayRefTile.getTile());
        set.arrayValues.add(ArrayValueTile.getTile());
        set.assigns.add(AssignmentTile.getTile());
        set.casts.add(CastNonPrimTile.getTile());
        set.casts.add(CastPrimTile.getTile());
        set.dcls.add(DclTile.getTile());
        set.divs.add(DivTile.getTile(true, true));
        set.eands.add(EBooleanTile.getTile(AndOpMaker.maker));
        set.eors.add(EBooleanTile.getTile(OrrOpMaker.maker));
        set.eqs.add(CompOpTile.getTile(Condition.EQ));
        set.ges.add(CompOpTile.getTile(Condition.GE));
        set.gts.add(CompOpTile.getTile(Condition.GT));
        set.les.add(CompOpTile.getTile(Condition.LE));
        set.lss.add(ShiftTile.getTile(Shift.LSL));
        set.rss.add(ShiftTile.getTile(Shift.ASR));
        set.urss.add(ShiftTile.getTile(Shift.LSR));
        set.lts.add(CompOpTile.getTile(Condition.LT));
        set.mults.add(SizedBinOpTile.getTile(MulOpMaker.maker));
        set.nameRefs.add(NameRefTile.getTile());
        set.nameValues.add(NameValueTile.getTile());
        set.negs.add(NegTile.getTile());
        set.nes.add(CompOpTile.getTile(Condition.NE));
        set.nots.add(NotTile.getTile());
        set.invokes.add(RegularCallTile.getTile());
        set.rems.add(DivTile.getTile(false, false));
        set.adds.add(StrAddTile.getTile());
        set.strs.add(StringTile.getTile());
        set.subs.add(SizedBinOpTile.getTile(SubOpMaker.maker));
        set.invokes.add(SuperCallTile.getTile());
    }
    //TODO add optomizations at some point
}
