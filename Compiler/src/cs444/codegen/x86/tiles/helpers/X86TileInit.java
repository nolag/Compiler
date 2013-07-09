package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.DivZeroTile;
import cs444.codegen.tiles.NonNullFieldAccess;
import cs444.codegen.tiles.ZeroMultTile;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.*;

public class X86TileInit {
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
        IfTile.init();
        InstanceOfTile.init();
        LETile.init();
        LSTile.init();
        RSTile.init();
        URSTile.init();
        LTTile.init();
        MethodTile.init();
        MultTile.init();
        NameRefTile.init();
        NameValueTile.init();
        NegTile.init();
        NeTile.init();
        NormalCreationTile.init();
        NotTile.init();
        NullTile.init();
        NumericalTile.init();
        OrTile.init();
        RegularCallTile.init();
        RemTile.init();
        RetTile.init();
        StaticCallTile.init();
        StrAddTile.init();
        StringTile.init();
        SubTile.init();
        SuperCallTile.init();
        ThisTile.init();
        WhileTile.init();
    }

    public static void initBasicOpt(){
        final Class<X86Instruction> klass = X86Instruction.class;
        NonNullFieldAccess.<X86Instruction, X86SizeHelper>init(klass);
        DivZeroTile.<X86Instruction, X86SizeHelper>init(klass);
        ZeroMultTile.<X86Instruction, X86SizeHelper>init(klass);
    }
}
