package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.TileInit;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.*;
import cs444.codegen.x86.tiles.opt.*;

public abstract class X86TileInit extends TileInit<X86Instruction, Size> {
    protected X86TileInit(final Class<? extends X86Platform> klass) {
        super(klass);
    }

    @Override
    public void initBase() {
        super.initBase();
        AddTile.init(klass);
        ArrayCreationTile.init(klass);
        ArrayRefTile.init(klass);
        ArrayValueTile.init(klass);
        AssignmentTile.init(klass);
        CastNonPrimTile.init(klass);
        CastPrimTile.init(klass);
        DclTile.init(klass);
        DivTile.init(klass);
        EAndTile.init(klass);
        EOrTile.init(klass);
        EQTile.init(klass);
        GETile.init(klass);
        GTTile.init(klass);
        LETile.init(klass);
        LSTile.init(klass);
        RSTile.init(klass);
        URSTile.init(klass);
        LTTile.init(klass);
        MultTile.init(klass);
        NameRefTile.init(klass);
        NameValueTile.init(klass);
        NegTile.init(klass);
        NeTile.init(klass);
        NotTile.init(klass);
        RegularCallTile.init(klass);
        RemTile.init(klass);
        StrAddTile.init(klass);
        StringTile.init(klass);
        SubTile.init(klass);
        SuperCallTile.init(klass);
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
