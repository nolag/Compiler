package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.tiles.generic.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.*;

public class X86TileInit {
    private static final Class<X86Instruction> klass = X86Instruction.class;

    public static void initBase(){
        AddTile.init();
        AndTile.init();
        ANonTerminalTile.init();
        ArrayCreationTile.init();
        ArrayRefTile.init();
        ArrayValueTile.init();
        AssignmentTile.init();
        BoolTile.init();
        CastNonPrimTile.init();
        CastPrimTile.init();
        ConstructorTile.init();
        DclTile.init();
        DivTile.init();
        EAndTile.init();
        EOrTile.init();
        EQTile.init();
        FieldAccessTile.init();
        ForTile.init();
        IfTile.<X86Instruction, Size>init(klass);
        InstanceOfTile.init();
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

    public static void initBasicOpt(){
        DivZeroTile.<X86Instruction, Size>init(klass);
        NonNullFieldAccess.<X86Instruction, Size>init(klass);
        ZeroMultTile.<X86Instruction, Size>init(klass);
    }
}
