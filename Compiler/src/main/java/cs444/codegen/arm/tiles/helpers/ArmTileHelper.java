package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.factories.Imm12OrRegMaker;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.APkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class ArmTileHelper extends TileHelper<ArmInstruction, Size> {
    public static final Push ENTER = new Push(Register.INTRA_PROCEDURE, Register.LINK);
    public static final Pop LEAVE = new Pop(Register.INTRA_PROCEDURE, Register.PC);

    private static final Push NATIVE_ENTER = new Push(Register.R8, Register.R9, Register.R10, Register.R11);
    private static final Pop NATIVE_LEAVE = new Pop(Register.R8, Register.R9, Register.R10, Register.R11);

    public static void setupJmpNull(Register reg, String ifNullLbl, SizeHelper<ArmInstruction, Size> sizeHelper,
                                    Addable<ArmInstruction> instructions) {
        instructions.add(new Comment("check if null"));
        instructions.add(new Cmp(reg, Immediate8.NULL, sizeHelper));
        instructions.add(new B(Condition.EQ, ifNullLbl));
    }

    public static void setupJumpNe(Register reg, Immediate8 when, String lblTo,
                                   SizeHelper<ArmInstruction, Size> sizeHelper,
                                   Addable<ArmInstruction> instructions) {
        instructions.add(new Cmp(reg, when, sizeHelper));
        instructions.add(new B(Condition.NE, lblTo));
    }

    public static Operand2 setupOp2(Register pref, int n, Addable<ArmInstruction> instructions,
                                    SizeHelper<ArmInstruction, Size> sizeHelper) {
        if (n >= 0) {
            if (n <= 255) {
                return new Immediate8((char) n);
            }
            //TODO other number optimizations that are shifts
        }

        int ival = n;
        instructions.add(new Movw(pref, new Immediate16(ival & 0xFFFF), sizeHelper));
        instructions.add(new Movt(pref, new Immediate16(ival >>> 16), sizeHelper));
        return pref;
    }

    public static void makeInstruction(Register dest, Register r1, Register pref, int val1,
                                       Imm12OrRegMaker maker, Addable<ArmInstruction> instructions,
                                       SizeHelper<ArmInstruction, Size> sizeHelper) {
        if (val1 <= 4095) {
            instructions.add(maker.make(dest, r1, new Immediate12((short) val1), sizeHelper));
            return;
        }
        setupNumberLoad(pref, val1, instructions, sizeHelper);
        instructions.add(maker.make(dest, r1, pref, sizeHelper));
    }

    public static void setupNumberLoad(Register dest, int n, Addable<ArmInstruction> instructions,
                                       SizeHelper<ArmInstruction, Size> sizeHelper) {
        if (n >= 0) {
            if (n <= 65535) {
                instructions.add(new Movw(dest, new Immediate16((char) n), sizeHelper));
                return;
            }
            //TODO other number optimizations that are shifts
        }

        int ival = n;
        instructions.add(new Movw(dest, new Immediate16(ival & 0xFFFF), sizeHelper));
        instructions.add(new Movt(dest, new Immediate16(ival >>> 16), sizeHelper));
    }

    @Override
    public void setupJmpNull(String ifNullLbl, SizeHelper<ArmInstruction, Size> sizeHelper,
                             Addable<ArmInstruction> instructions) {
        setupJmpNull(Register.R0, ifNullLbl, sizeHelper, instructions);
    }

    @Override
    public void methProlog(MethodOrConstructorSymbol method, String methodName,
                           SizeHelper<ArmInstruction, Size> sizeHelper,
                           Addable<ArmInstruction> instructions) {
        if (method.getProtectionLevel() != ProtectionLevel.PRIVATE) {
            instructions.add(new Global(methodName));
        }
        instructions.add(new Label(methodName));
        instructions.add(ENTER);
        instructions.add(new Mov(Register.INTRA_PROCEDURE, Register.STACK, sizeHelper));
    }

    @Override
    public void methEpilogue(SizeHelper<ArmInstruction, Size> sizeHelper, Addable<ArmInstruction> instructions) {
        instructions.add(new Mov(Register.STACK, Register.INTRA_PROCEDURE, sizeHelper));
        instructions.add(LEAVE);
    }

    @Override
    public void setupJumpNe(String lblTo, SizeHelper<ArmInstruction, Size> sizeHelper,
                            Addable<ArmInstruction> instructions) {
        setupJumpNe(Register.R0, Immediate8.TRUE, lblTo, sizeHelper, instructions);
    }

    @Override
    public void setupJumpNeFalse(String lblTo, SizeHelper<ArmInstruction, Size> sizeHelper,
                                 Addable<ArmInstruction> instructions) {
        setupJumpNe(Register.R0, Immediate8.FALSE, lblTo, sizeHelper, instructions);
    }

    @Override
    public void invokeConstructor(APkgClassResolver resolver, List<ISymbol> children,
                                  Platform<ArmInstruction, Size> platform,
                                  Addable<ArmInstruction> instructions) {
        List<String> types = new LinkedList<String>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Back up addr of obj in Base so it is safe"));
        instructions.add(new Push(Register.R8));
        instructions.add(new Mov(Register.R8, Register.R0, sizeHelper));

        for (ISymbol child : children) {
            instructions.addAll(platform.getBest(child));
            Typeable arg = (Typeable) child;
            TypeSymbol ts = arg.getType();
            if (ts.value.equals(JoosNonTerminal.LONG)) {
                pushLong(arg, instructions, sizeHelper);
            } else {
                instructions.add(new Push(Register.R0));
            }
            types.add(ts.getTypeDclNode().fullName);
        }

        instructions.add(new Push(Register.R8));

        ConstructorSymbol cs = null;
        try {
            cs = resolver.getConstructor(types, resolver);
        } catch (UndeclaredException e) {
            //Should never get here
            e.printStackTrace();
            return;
        }

        String arg = APkgClassResolver.generateFullId(cs);
        if (resolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions.add(new Extern(
                    new ImmediateStr(arg)));
        }

        instructions.add(new Bl(arg));
        //return value is the new object
        instructions.add(new Pop(Register.R0));

        int mySize = (int) cs.getStackSize(platform) - sizeHelper.getDefaultStackSize();
        if (mySize != 0) {
            Operand2 op2 = setupOp2(Register.R0, mySize, instructions, sizeHelper);
            instructions.add(new Add(Register.STACK, Register.STACK, op2, sizeHelper));
        }
        instructions.add(new Comment("Restore Base register"));
        instructions.add(new Pop(Register.R8));
    }

    @Override
    public void strPartHelper(ISymbol child, APkgClassResolver resolver, Addable<ArmInstruction> instructions,
                              Platform<ArmInstruction, Size> platform) {
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        String firstType = ((Typeable) child).getType().getTypeDclNode().fullName;
        instructions.addAll(platform.getBest(child));
        AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(firstType), false);
        if (ms == null) {
            ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(JoosNonTerminal.OBJECT), false);
        }

        Size lastSize = sizeHelper.getPushSize(sizeHelper.getSize(sizeHelper.getByteSizeOfType(firstType)));
        int pop = sizeHelper.getIntSize(lastSize);

        instructions.add(new Push(Register.R0));

        String arg = APkgClassResolver.generateFullId(ms);

        if (ms.dclInResolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions.add(new Extern(
                    new ImmediateStr(arg)));
        }
        instructions.add(new Bl(arg));

        Operand2 op2 = setupOp2(Register.R0, pop, instructions, sizeHelper);
        instructions.add(new Add(Register.STACK, Register.STACK, op2, sizeHelper));
    }

    @Override
    public void callStartHelper(SimpleMethodInvoke invoke, Addable<ArmInstruction> instructions,
                                Platform<ArmInstruction, Size> platform) {
        MethodOrConstructorSymbol call = invoke.call;
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        if (call.isNative()) {
            instructions.add(new Comment("Backing up registers that are to be saved"));
            instructions.add(NATIVE_ENTER);
        }

        instructions.add(new Comment("Pushing args"));

        for (ISymbol isymbol : invoke.children) {
            Typeable arg = (Typeable) isymbol;
            TypeSymbol ts = arg.getType();
            instructions.addAll(platform.getBest(arg));
            //I don't see a way to push 2 bytes...
            if (ts.value.equals(JoosNonTerminal.LONG)) {
                pushLong(arg, instructions, sizeHelper);
            } else {
                instructions.add(new Push(Register.R0));
            }
        }
    }

    @Override
    public void callEndHelper(MethodOrConstructorSymbol call, Addable<ArmInstruction> instructions,
                              Platform<ArmInstruction, Size> platform) {
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        // NOTE: do not use INVOKE in here, invoke gets size from method,
        // but visitor may visit InvokeSymbol before MethodSymbol
        int mySize = (int) call.getStackSize(platform);
        if (mySize != 0) {
            instructions.add(new Add(Register.STACK, Register.STACK, setupOp2(Register.R0, mySize, instructions,
                    sizeHelper), sizeHelper));
        }

        if (call.isNative()) {
            instructions.add(new Comment("Restoring up registers that are to be saved"));
            instructions.add(NATIVE_LEAVE);
        }

        instructions.add(new Comment("end invoke"));
    }

    @Override
    public void setupJump(String lblTo, SizeHelper<ArmInstruction, Size> sizeHelper,
                          Addable<ArmInstruction> instructions) {
        instructions.add(new B(lblTo));
    }

    @Override
    public void setupComment(String comment, Addable<ArmInstruction> instructions) {
        instructions.add(new Comment(comment));
    }

    @Override
    public void setupLbl(String lbl, Addable<ArmInstruction> instructions) {
        instructions.add(new Label(lbl));
    }

    @Override
    public void setupExtern(String extern, Addable<ArmInstruction> instructions) {
        instructions.add(new Extern(new ImmediateStr(extern)));
    }

    @Override
    public void loadBool(boolean bool, Addable<ArmInstruction> instructions,
                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        instructions.add(new Movw(Register.R0, (Immediate16) (bool ? Immediate8.TRUE : Immediate8.FALSE), sizeHelper));
    }

    @Override
    public void allocateStackSpace(String what, Addable<ArmInstruction> instructions, long amount,
                                   Platform<ArmInstruction, Size> platform) {
        if (0 != amount) {
            SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
            instructions.add(platform.makeComment("add stack space for " + what));
            instructions.add(new Sub(Register.STACK, Register.STACK, setupOp2(Register.R0, (int) amount, instructions
                    , sizeHelper),
                    sizeHelper));
        }
    }

    @Override
    public void cleanStackSpace(String what, Addable<ArmInstruction> instructions, long amount,
                                Platform<ArmInstruction, Size> platform) {
        if (0 != amount) {
            SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
            instructions.add(platform.makeComment("clean stack space from " + what));
            instructions.add(new Add(Register.STACK, Register.STACK, setupOp2(Register.R0, (int) amount, instructions
                    , sizeHelper),
                    sizeHelper));
        }
    }

    @Override
    public void loadThisToDefault(Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        instructions.add(new Comment("This (or super) pointer"));
        instructions.add(new Ldr(Register.R0, Register.INTRA_PROCEDURE,
                new Immediate12((short) (sizeHelper.getDefaultStackSize() * 2)),
                sizeHelper));
    }

    @Override
    public void makeCall(String to, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        instructions.add(new Bl(to));
    }

    @Override
    public void setupStaticHeader(Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        instructions.add(new Push(Register.LINK));
    }

    @Override
    public void setupStaticFooter(Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        instructions.add(new Pop(Register.LINK));
    }
}
