package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.TileInit;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.*;
import cs444.codegen.x86.tiles.*;
import cs444.codegen.x86.tiles.opt.AddWithConstTile;
import cs444.codegen.x86.tiles.opt.BinWithConstTile;
import cs444.codegen.x86.tiles.opt.BinWithConstTile.Side;
import cs444.codegen.x86.tiles.opt.CompWithConstTile;
import cs444.codegen.x86.tiles.opt.EBooleanWithConstTile;
import cs444.codegen.x86.tiles.opt.LeaForMultOffOneTile;
import cs444.codegen.x86.tiles.opt.LeaForMultTile;
import cs444.codegen.x86.tiles.opt.MultPow2Tile;
import cs444.parser.symbols.ast.expressions.*;

public abstract class X86TileInit extends TileInit<X86Instruction, Size> {
    protected X86TileInit(final Class<? extends X86Platform> klass) {
        super(klass);
    }

    @Override
    public void initBase() {
        super.initBase();
        final TileSet<X86Instruction, Size> set = TileSet.<X86Instruction, Size> getOrMake(klass);
        set.adds.add(AddTile.getTile());
        set.creation.add(ArrayCreationTile.getTile());
        set.arrayRefs.add(ArrayRefTile.getTile());
        set.arrayValues.add(ArrayValueTile.getTile());
        set.assigns.add(AssignmentTile.getTile());
        set.casts.add(CastNonPrimTile.getTile());
        set.casts.add(CastPrimTile.getTile());
        set.dcls.add(DclTile.getTile());
        set.divs.add(BinUniOpTile.<DivideExprSymbol> getTile(IDivMaker.maker, true, DivideExprSymbol.class));
        set.eands.add(EBooleanTile.<EAndExprSymbol> getTile(AndOpMaker.maker));
        set.eors.add(EBooleanTile.<EOrExprSymbol> getTile(OrOpMaker.maker));
        set.eqs.add(CompOpTile.<EqExprSymbol> getTile(SeteMaker.maker, false, EqExprSymbol.class));
        set.ges.add(CompOpTile.<GeExprSymbol> getTile(SetleMaker.maker, false, GeExprSymbol.class));
        set.gts.add(CompOpTile.<GtExprSymbol> getTile(SetlMaker.maker, false, GtExprSymbol.class));
        set.les.add(CompOpTile.<LeExprSymbol> getTile(SetgeMaker.maker, false, LeExprSymbol.class));
        set.lss.add(SizedBinOpTile.<LSExprSymbol> getTile(LSOpMaker.maker, true, LSExprSymbol.class));
        set.rss.add(SizedBinOpTile.<RSExprSymbol> getTile(RSOpMaker.maker, true, RSExprSymbol.class));
        set.urss.add(SizedBinOpTile.<URSExprSymbol> getTile(URSOpMaker.maker, true, URSExprSymbol.class));
        set.lts.add(CompOpTile.<LtExprSymbol> getTile(SetgMaker.maker, false, LtExprSymbol.class));
        set.mults.add(BinUniOpTile.<MultiplyExprSymbol> getTile(IMulMaker.maker, false, MultiplyExprSymbol.class));
        set.nameRefs.add(NameRefTile.getTile());
        set.nameValues.add(NameValueTile.getTile());
        set.negs.add(NegTile.getTile());
        set.nes.add(CompOpTile.<NeExprSymbol> getTile(SetneMaker.maker, false, NeExprSymbol.class));
        set.nots.add(NotTile.getTile());
        set.invokes.add(RegularCallTile.getTile());
        set.rems.add(RemTile.getTile());
        set.adds.add(StrAddTile.getTile());
        set.strs.add(StringTile.getTile());
        set.subs.add(SizedBinOpTile.<SubtractExprSymbol> getTile(SubOpMaker.maker, true, SubtractExprSymbol.class));
        set.invokes.add(SuperCallTile.getTile());
    }

    @Override
    public void initBasicOpt() {
        super.initBasicOpt();
        final TileSet<X86Instruction, Size> set = TileSet.<X86Instruction, Size> getOrMake(klass);

        set.adds.add(AddWithConstTile.getTile());
        set.eands.add(EBooleanWithConstTile.<EAndExprSymbol> getTile(AndOpMaker.maker));
        set.eors.add(EBooleanWithConstTile.<EOrExprSymbol> getTile(OrOpMaker.maker));
        set.eqs.add(CompWithConstTile.<EqExprSymbol> getTile(SeteMaker.maker, Side.EITHER, EqExprSymbol.class));
        set.ges.add(CompWithConstTile.<GeExprSymbol> getTile(SetleMaker.maker, Side.LEFT, GeExprSymbol.class));
        set.ges.add(CompWithConstTile.<GeExprSymbol> getTile(SetgeMaker.maker, Side.RIGHT, GeExprSymbol.class));
        set.gts.add(CompWithConstTile.<GtExprSymbol> getTile(SetlMaker.maker, Side.LEFT, GtExprSymbol.class));
        set.gts.add(CompWithConstTile.<GtExprSymbol> getTile(SetgMaker.maker, Side.RIGHT, GtExprSymbol.class));
        set.mults.add(LeaForMultTile.getTile());
        set.mults.add(LeaForMultOffOneTile.getTile());
        set.les.add(CompWithConstTile.<LeExprSymbol> getTile(SetgeMaker.maker, Side.LEFT, LeExprSymbol.class));
        set.les.add(CompWithConstTile.<LeExprSymbol> getTile(SetleMaker.maker, Side.RIGHT, LeExprSymbol.class));
        set.lts.add(CompWithConstTile.<LtExprSymbol> getTile(SetgMaker.maker, Side.LEFT, LtExprSymbol.class));
        set.lts.add(CompWithConstTile.<LtExprSymbol> getTile(SetlMaker.maker, Side.RIGHT, LtExprSymbol.class));
        set.mults.add(MultPow2Tile.getTile());
        set.nes.add(CompWithConstTile.<NeExprSymbol> getTile(SetneMaker.maker, Side.EITHER, NeExprSymbol.class));
        set.subs.add(BinWithConstTile.<SubtractExprSymbol> getTile(SubOpMaker.maker, Side.RIGHT, SubtractExprSymbol.class));
    }
}
