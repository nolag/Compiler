package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.TileInit;
import cs444.codegen.generic.tiles.*;
import cs444.codegen.generic.tiles.opt.DivZeroTile;
import cs444.codegen.generic.tiles.opt.NonNullFieldAccess;
import cs444.codegen.generic.tiles.opt.UpCastTile;
import cs444.codegen.generic.tiles.opt.ZeroMultTile;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.*;

public abstract class X86TileInit extends TileInit{
    protected static final Class<X86Instruction> klass = X86Instruction.class;

    protected X86TileInit(){ }

    @Override
    public void initBase(){
        AddTile.init();
        AndTile.<X86Instruction, Size>init(klass);
        ANonTerminalTile.init();
        ArrayCreationTile.init();
        ArrayRefTile.init();
        ArrayValueTile.init();
        AssignmentTile.init();
        BoolTile.<X86Instruction, Size>init(klass);
        CastNonPrimTile.init();
        CastPrimTile.init();
        ConstructorTile.init();
        DclTile.init();
        DivTile.init();
        EAndTile.init();
        EOrTile.init();
        EQTile.init();
        FieldAccessTile.<X86Instruction, Size>init(klass);
        ForTile.init();
        IfTile.<X86Instruction, Size>init(klass);
        InstanceOfTile.<X86Instruction, Size>init(klass);
        LETile.init();
        LSTile.init();
        RSTile.init();
        URSTile.init();
        LTTile.init();
        MethodTile.<X86Instruction, Size>init(klass);
        MultTile.init();
        NameRefTile.init();
        NameValueTile.init();
        NegTile.init();
        NeTile.init();
        NormalCreationTile.init();
        NotTile.init();
        NullTile.init();
        NumericalTile.init();
        OrTile.<X86Instruction, Size>init(klass);
        RegularCallTile.init();
        RemTile.init();
        RetTile.init();
        StaticCallTile.init();
        StrAddTile.init();
        StringTile.init();
        SubTile.init();
        SuperCallTile.init();
        ThisTile.init();
        WhileTile.<X86Instruction, Size>init(klass);
    }

    @Override
    public void initBasicOpt(){
        DivZeroTile.<X86Instruction, Size>init(klass);
        NonNullFieldAccess.<X86Instruction, Size>init(klass);
        UpCastTile.<X86Instruction, Size>init(klass);
        ZeroMultTile.<X86Instruction, Size>init(klass);
    }
}
