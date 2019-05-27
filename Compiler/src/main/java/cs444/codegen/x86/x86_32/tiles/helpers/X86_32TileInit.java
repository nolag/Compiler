package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.*;
import cs444.codegen.x86.tiles.helpers.X86TileInit;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.*;
import cs444.parser.symbols.ast.expressions.*;

public class X86_32TileInit extends X86TileInit {
    public static final X86_32TileInit instance = new X86_32TileInit();

    private X86_32TileInit() {
        super(X86_32Platform.class);
    }

    @Override
    public void initBase() {
        super.initBase();
        TileSet<X86Instruction, Size> set = TileSet.getOrMake(X86_32Platform.class);
        set.casts.add(CastFromLongTile.getTile());
        set.adds.add(LongBinTile.getTile(AddOpMaker.maker, AdcOpMaker.maker, false,
                AddExprSymbol.class));
        set.arrayValues.add(LongArrayValueTile.getTile());
        set.assigns.add(LongAssignmentTile.getTile());
        set.dcls.add(LongDclTile.getTile());
        set.divs.add(LongDivTile.getTile(true, true));
        set.eqs.add(LongJxxTile.getTile(null, JneMaker.maker, JeMaker.maker, EqExprSymbol.class));
        set.ges.add(LongJxxTile.getTile(JlMaker.maker, JgMaker.maker, JbeMaker.maker,
                GeExprSymbol.class));
        set.gts.add(LongJxxTile.getTile(JlMaker.maker, JgMaker.maker, JbMaker.maker, GtExprSymbol.class));
        set.lss.add(LongLeftShift.getTile());
        set.les.add(LongJxxTile.getTile(JgMaker.maker, JlMaker.maker, JaeMaker.maker,
                LeExprSymbol.class));
        set.lts.add(LongJxxTile.getTile(JgMaker.maker, JlMaker.maker, JaMaker.maker, LtExprSymbol.class));
        set.mults.add(LongMultTile.getTile());
        set.nameValues.add(LongNameValueTile.getTile());
        set.negs.add(LongNegTile.getTile());
        set.nes.add(LongJxxTile.getTile(JneMaker.maker, null, JneMaker.maker, NeExprSymbol.class));
        set.rems.add(LongDivTile.getTile(false, false));
        set.rss.add(LongRightShiftTile.getTile());
        set.subs.add(LongBinTile.getTile(SubOpMaker.maker, SbbOpMaker.maker, true,
                SubtractExprSymbol.class));
        set.urss.add(LongUnsignedRightShift.getTile());
        set.nameValues.add(StaticNameValue.getTile());
    }
}
