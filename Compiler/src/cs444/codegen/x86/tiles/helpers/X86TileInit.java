package cs444.codegen.x86.tiles.helpers;

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
}
