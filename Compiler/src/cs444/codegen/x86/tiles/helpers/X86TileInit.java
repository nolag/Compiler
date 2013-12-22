package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.TileInit;
import cs444.codegen.generic.tiles.*;
import cs444.codegen.generic.tiles.opt.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.*;
import cs444.codegen.x86.tiles.opt.LeaForMultTile;
import cs444.codegen.x86.tiles.opt.LeaForMultOffOneTile;
import cs444.codegen.x86.tiles.opt.MultPow2Tile;

public abstract class X86TileInit extends TileInit {
    protected final Class<? extends X86Platform> klass;

    protected X86TileInit(final Class<? extends X86Platform> klass) {
        this.klass =klass;
    }

    @Override
    public void initBase() {
        AddTile.init(klass);
        AndTile.<X86Instruction, Size>init(klass);
        ANonTerminalTile.init(klass);
        ArrayCreationTile.init(klass);
        ArrayRefTile.init(klass);
        ArrayValueTile.init(klass);
        AssignmentTile.init(klass);
        BoolTile.<X86Instruction, Size>init(klass);
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
        FieldAccessTile.<X86Instruction, Size>init(klass);
        ForTile.init(klass);
        IfTile.<X86Instruction, Size>init(klass);
        InstanceOfTile.<X86Instruction, Size>init(klass);
        LETile.init(klass);
        LongCastTile.init(klass);
        LSTile.init(klass);
        RSTile.init(klass);
        URSTile.init(klass);
        LTTile.init(klass);
        MethodTile.<X86Instruction, Size>init(klass);
        MultTile.init(klass);
        NameRefTile.init(klass);
        NameValueTile.init(klass);
        NegTile.init(klass);
        NeTile.init(klass);
        NormalCreationTile.init(klass);
        NotTile.init(klass);
        NullTile.init(klass);
        NumericalTile.init(klass);
        OrTile.<X86Instruction, Size>init(klass);
        RegularCallTile.init(klass);
        RemTile.init(klass);
        RetTile.init(klass);
        StaticCallTile.init(klass);
        StrAddTile.init(klass);
        StringTile.init(klass);
        SubTile.init(klass);
        SuperCallTile.init(klass);
        ThisTile.init(klass);
        WhileTile.<X86Instruction, Size>init(klass);
    }

    @Override
    public void initBasicOpt() {
        DivZeroTile.<X86Instruction, Size>init(klass);
        LeaForMultTile.init(klass);
        LeaForMultOffOneTile.init(klass);
        MultPow2Tile.init(klass);
        NonNullFieldAccess.<X86Instruction, Size>init(klass);
        RemZeroTile.<X86Instruction, Size>init(klass);
        UpCastTile.<X86Instruction, Size>init(klass);
        ZeroMultTile.<X86Instruction, Size>init(klass);
    }
}
