package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.factories.AddOpMaker;
import cs444.codegen.arm.instructions.factories.MulOpMaker;
import cs444.codegen.arm.instructions.factories.OrrOpMaker;
import cs444.codegen.arm.instructions.factories.SubOpMaker;
import cs444.codegen.arm.tiles.*;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.*;

public abstract class ArmTileInit extends TileInit<ArmInstruction, Size> {

    protected ArmTileInit(Class<? extends Platform<ArmInstruction, Size>> klass) {
        super(klass);
    }

    @Override
    public void initBase() {
        super.initBase();
        //TODO tiles
        final TileSet<ArmInstruction, Size> set = TileSet.<ArmInstruction, Size> getOrMake(klass);
        set.adds.add(AddTile.getTile());
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
        set.eqs.add(CompOpTile.<EqExprSymbol> getTile(Condition.EQ));
        set.ges.add(CompOpTile.<GeExprSymbol> getTile(Condition.GE));
        set.gts.add(CompOpTile.<GtExprSymbol> getTile(Condition.GT));
        set.les.add(CompOpTile.<LeExprSymbol> getTile(Condition.LE));
        set.lss.add(ShiftTile.<LSExprSymbol> getTile(Shift.LSL));
        set.rss.add(ShiftTile.<RSExprSymbol> getTile(Shift.ASR));
        set.urss.add(ShiftTile.<URSExprSymbol> getTile(Shift.LSR));
        set.lts.add(CompOpTile.<LtExprSymbol> getTile(Condition.LT));
        set.mults.add(SizedBinOpTile.<MultiplyExprSymbol> getTile(MulOpMaker.maker));
        set.nameRefs.add(NameRefTile.getTile());
        set.nameValues.add(NameValueTile.getTile());
        set.negs.add(NegTile.getTile());
        set.nes.add(CompOpTile.<NeExprSymbol> getTile(Condition.NE));
        set.nots.add(NotTile.getTile());
        set.invokes.add(RegularCallTile.getTile());
        set.rems.add(RemTile.getTile());
        set.adds.add(StrAddTile.getTile());
        set.strs.add(StringTile.getTile());
        set.subs.add(SizedBinOpTile.<SubtractExprSymbol> getTile(SubOpMaker.maker));
        set.invokes.add(SuperCallTile.getTile());
    }
    //TODO add optomizations at some point
}
