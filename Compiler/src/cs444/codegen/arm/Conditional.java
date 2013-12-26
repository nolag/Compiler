package cs444.codegen.arm;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.instructions.bases.ARMInstruction;

public class Conditional extends SimpleInstructionArg {
    public static final Conditional EQ = new Conditional("EQ");
    public static final Conditional NE = new Conditional("NE");
    public static final Conditional HS = new Conditional("HS");
    public static final Conditional LO = new Conditional("LO");
    public static final Conditional MI = new Conditional("MI");
    public static final Conditional PL = new Conditional("PL");
    public static final Conditional VS = new Conditional("VS");
    public static final Conditional VC = new Conditional("VC");
    public static final Conditional HI = new Conditional("HI");
    public static final Conditional LS = new Conditional("LS");
    public static final Conditional GE = new Conditional("GE");
    public static final Conditional LT = new Conditional("LT");
    public static final Conditional GT = new Conditional("GT");
    public static final Conditional LE = new Conditional("LE");
    //AL is always and can be used, but there is no point including it.  Making it in case there is a helper that needs a conditional.
    public static final Conditional AL = new Conditional("");
    
    public final String when;
    
    private Conditional(final String when) { 
        this.when = when;
    }

    @Override
    public String getValue(Size size, SizeHelper<ARMInstruction, Size> sizeHelper) {
        return when;
    }
}
