package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.*;
import cs444.codegen.x86.tiles.helpers.X86TileInit;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.*;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;
import cs444.parser.symbols.ast.expressions.GeExprSymbol;
import cs444.parser.symbols.ast.expressions.GtExprSymbol;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class X86_32TileInit extends X86TileInit {
    public static final X86_32TileInit instance = new X86_32TileInit();

    private X86_32TileInit() {
        super(X86_32Platform.class);
    }

    @Override
    public void initBase() {
        super.initBase();
        final TileSet<X86Instruction, Size> set = TileSet.getOrMake(X86_32Platform.class);
        set.casts.add(CastFromLongTile.getTile());
        set.adds.add(LongBinTile.<AddExprSymbol> getTile(AddOpMaker.maker, AdcOpMaker.maker, false, AddExprSymbol.class));
        set.arrayValues.add(LongArrayValueTile.getTile());
        set.assigns.add(LongAssignmentTile.getTile());
        set.dcls.add(LongDclTile.getTile());
        set.divs.add(LongDivTile.getTile());
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
        set.rss.add(LongRightShiftTile.getTile());
        set.subs.add(LongBinTile.<SubtractExprSymbol> getTile(SubOpMaker.maker, SbbOpMaker.maker, true, SubtractExprSymbol.class));
        set.urss.add(LongUnsignedRightShift.getTile());
        set.nameValues.add(StaticNameValue.getTile());
    }
}
