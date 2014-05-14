package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.*;

public class ArmTileInit extends TileInit<ArmInstruction, Size> {
    
    
    protected ArmTileInit(Class<? extends Platform<ArmInstruction, Size>> klass) {
        super(klass);
    }
    
    @Override
    public void initBase() {
        super.initBase();
        AddTile.init(klass);
        
        /*X86 will need similar
        ANonTerminalTile.init(klass);
        ArrayCreationTile.init(klass);
        ArrayRefTile.init(klass);
        ArrayValueTile.init(klass);
        AssignmentTile.init(klass);
        CastNonPrimTile.init(klass);
        CastPrimTile.init(klass);
        ConstructorTile.init(klass);
        DclTile.init(klass);
        DivTile.init(klass);*/
        
        EAndTile.init(klass);
        EOrTile.init(klass);
        
        /*EQTile.init(klass);
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
        StringTile.init(klass);*/
        SubTile.init(klass);
        /*SuperCallTile.init(klass);
        ThisTile.init(klass);
         */
        
        /*X86_32, may need similar
        CastFromLongTile.init();
        LongAddTile.init();
        LongArrayValueTile.init();
        LongAssignmentTile.init();
        LongDclTile.init();
        LongDivTile.init();
        LongEqTile.init();
        LongGeTile.init();
        LongGtTile.init();
        LongLeftShift.init();
        LongLeTile.init();
        LongLtTile.init();
        LongMultTile.init();
        LongNameValueTile.init();
        LongNegTile.init();
        LongNeTile.init();
        LongNumberTile.init();
        LongRemTile.init();
        LongRigthShiftTile.init();
        LongSubTile.init();
        LongUnsignedRightShift.init();
        StaticNameValue.init();
         */
        
        /* X86_64, may need similar
         StaticNameValue.init();
         */
    }
    
    //TODO add optomizations at some point
}
