package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.TileInit;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.*;
import cs444.codegen.x86.tiles.opt.AddWithConstTile;
import cs444.codegen.x86.tiles.opt.EAndWithConstTile;
import cs444.codegen.x86.tiles.opt.EOrWithConstTile;
import cs444.codegen.x86.tiles.opt.EQWithConstTile;
import cs444.codegen.x86.tiles.opt.GELHSConstTile;
import cs444.codegen.x86.tiles.opt.GERHSConstTile;
import cs444.codegen.x86.tiles.opt.GTLHSConstTile;
import cs444.codegen.x86.tiles.opt.GTRHSConstTile;
import cs444.codegen.x86.tiles.opt.LELHSConstTile;
import cs444.codegen.x86.tiles.opt.LERHSConstTile;
import cs444.codegen.x86.tiles.opt.LTLHSConstTile;
import cs444.codegen.x86.tiles.opt.LTRHSConstTile;
import cs444.codegen.x86.tiles.opt.LeaForMultTile;
import cs444.codegen.x86.tiles.opt.LeaForMultOffOneTile;
import cs444.codegen.x86.tiles.opt.MultPow2Tile;
import cs444.codegen.x86.tiles.opt.NeWithConstTile;
import cs444.codegen.x86.tiles.opt.SubRHSConstTile;

public abstract class X86TileInit extends TileInit<X86Instruction, Size> {
    protected final Class<? extends X86Platform> klass;

    protected X86TileInit(final Class<? extends X86Platform> klass) {
        super(klass);
        this.klass =klass;
    }

    @Override
    public void initBase() {
        super.initBase();
        AddTile.init(klass);
        ANonTerminalTile.init(klass);
        ArrayCreationTile.init(klass);
        ArrayRefTile.init(klass);
        ArrayValueTile.init(klass);
        AssignmentTile.init(klass);
        CastNonPrimTile.init(klass);
        CastPrimTile.init(klass);
        ConstructorTile.init(klass);
        DclTile.init(klass);
        DivTile.init(klass);
        EAndTile.init(klass);
        EOrTile.init(klass);
        EQTile.init(klass);
        GETile.init(klass);
        GTTile.init(klass);
        ForTile.init(klass);
        LETile.init(klass);
        LongCastTile.init(klass);
        LSTile.init(klass);
        RSTile.init(klass);
        URSTile.init(klass);
        LTTile.init(klass);
        MultTile.init(klass);
        NameRefTile.init(klass);
        NameValueTile.init(klass);
        NegTile.init(klass);
        NeTile.init(klass);
        NormalCreationTile.init(klass);
        NotTile.init(klass);
        NullTile.init(klass);
        NumericalTile.init(klass);
        RegularCallTile.init(klass);
        RemTile.init(klass);
        RetTile.init(klass);
        StaticCallTile.init(klass);
        StrAddTile.init(klass);
        StringTile.init(klass);
        SubTile.init(klass);
        SuperCallTile.init(klass);
        ThisTile.init(klass);
    }

    @Override
    public void initBasicOpt() {
        super.initBasicOpt();
        AddWithConstTile.init(klass);
        EAndWithConstTile.init(klass);
        EOrWithConstTile.init(klass);
        EQWithConstTile.init(klass);
        GELHSConstTile.init(klass);
        GERHSConstTile.init(klass);
        GTLHSConstTile.init(klass);
        GTRHSConstTile.init(klass);
        LeaForMultTile.init(klass);
        LeaForMultOffOneTile.init(klass);
        LELHSConstTile.init(klass);
        LERHSConstTile.init(klass);
        LTLHSConstTile.init(klass);
        LTRHSConstTile.init(klass);
        MultPow2Tile.init(klass);
        NeWithConstTile.init(klass);
        SubRHSConstTile.init(klass);
    }
}
