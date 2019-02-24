package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.UniOpMaker;
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

public abstract class X86TileHelper extends TileHelper<X86Instruction, Size> {

    protected X86TileHelper() {}

    public static void genMov(Size size, Register from, String value, Typeable dcl,
                              SizeHelper<X86Instruction, Size> sizeHelper,
                              Addable<X86Instruction> instructions) {

        X86Instruction instruction;

        if (size == sizeHelper.getDefaultSize()) {
            instruction = new Mov(Register.ACCUMULATOR, from, sizeHelper);
        } else if (JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)) {
            instruction = new Movzx(Register.ACCUMULATOR, from, size, sizeHelper);
        } else {
            instruction = new Movsx(Register.ACCUMULATOR, from, size, sizeHelper);
        }

        instructions.add(new Comment("getting value of " + value));
        instructions.add(instruction);
    }

    public static void genMov(Size size, Memory from, String value, Typeable dcl,
                              SizeHelper<X86Instruction, Size> sizeHelper,
                              Addable<X86Instruction> instructions) {

        X86Instruction instruction;

        if (size == sizeHelper.getDefaultSize()) {
            instruction = new Mov(Register.ACCUMULATOR, from, sizeHelper);
        } else if (JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)) {
            instruction = new Movzx(Register.ACCUMULATOR, from, size, sizeHelper);
        } else {
            instruction = new Movsx(Register.ACCUMULATOR, from, size, sizeHelper);
        }

        instructions.add(new Comment("getting value of " + value));
        instructions.add(instruction);
    }

    public static void genMov(Size size, Immediate from, String value, Typeable dcl,
                              SizeHelper<X86Instruction, Size> sizeHelper,
                              Addable<X86Instruction> instructions) {

        X86Instruction instruction;

        if (size == sizeHelper.getDefaultSize()) {
            instruction = new Mov(Register.ACCUMULATOR, from, sizeHelper);
        } else if (JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)) {
            instruction = new Movzx(Register.ACCUMULATOR, from, size, sizeHelper);
        } else {
            instruction = new Movsx(Register.ACCUMULATOR, from, size, sizeHelper);
        }

        instructions.add(new Comment("getting value of " + value));
        instructions.add(instruction);
    }

    public static void setupJmpNull(Register register, String ifNullLbl, SizeHelper<X86Instruction,
            Size> sizeHelper,
                                    Addable<X86Instruction> instructions) {
        instructions.add(new Comment("null check"));
        instructions.add(new Cmp(register, Immediate.NULL, sizeHelper));
        instructions.add(new Je(new Immediate(ifNullLbl), sizeHelper));
    }

    public static void setupJumpNe(Register reg, Immediate when, String lblTo,
                                   SizeHelper<X86Instruction, Size> sizeHelper,
                                   Addable<X86Instruction> instructions, Size size) {
        instructions.add(new Cmp(reg, when, sizeHelper, size));
        instructions.add(new Jne(new Immediate(lblTo), sizeHelper));
    }

    public static void addCompEnding(Addable<X86Instruction> instructions, UniOpMaker uni,
                                     Platform<X86Instruction, Size> platform) {

        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Comment("Xor here CAN change the setl bit?"));
        instructions.add(uni.make(Register.DATA, sizeHelper, sizeHelper.getDefaultSize()));
        instructions.add(new Comment("clear all bits in register"));
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, Size.LOW, sizeHelper));
    }

    @Override
    public final void setupJmpNull(String ifNullLbl, SizeHelper<X86Instruction, Size> sizeHelper,
                                   Addable<X86Instruction> instructions) {

        setupJmpNull(Register.ACCUMULATOR, ifNullLbl, sizeHelper, instructions);
    }

    @Override
    public void methProlog(MethodOrConstructorSymbol method, String methodName,
                           SizeHelper<X86Instruction, Size> sizeHelper,
                           Addable<X86Instruction> instructions) {

        if (method.getProtectionLevel() != ProtectionLevel.PRIVATE) {
            instructions.add(new Global(methodName));
        }
        instructions.add(new Label(methodName));
        instructions.add(Push.getStackPush(sizeHelper));
        instructions.add(new Mov(Register.FRAME, Register.STACK, sizeHelper));
    }

    @Override
    public void methEpilogue(SizeHelper<X86Instruction, Size> sizeHelper,
                             Addable<X86Instruction> instructions) {
        instructions.add(Leave.LEAVE);
        instructions.add(Ret.RET);
    }

    @Override
    public final void setupJumpNe(String lblTo, SizeHelper<X86Instruction, Size> sizeHelper,
                                  Addable<X86Instruction> instructions) {

        setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, lblTo, sizeHelper, instructions, sizeHelper.getDefaultSize());
    }

    @Override
    public final void setupJumpNeFalse(String lblTo, SizeHelper<X86Instruction, Size> sizeHelper,
                                       Addable<X86Instruction> instructions) {

        setupJumpNe(Register.ACCUMULATOR, Immediate.FALSE, lblTo, sizeHelper, instructions,
                sizeHelper.getDefaultSize());
    }

    @Override
    public void invokeConstructor(APkgClassResolver resolver, List<ISymbol> children,
                                  Platform<X86Instruction, Size> platform,
                                  Addable<X86Instruction> instructions) {

        List<String> types = new LinkedList<String>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Back up addr of obj in Base so it is safe"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        for (ISymbol child : children) {
            instructions.addAll(platform.getBest(child));
            Typeable arg = (Typeable) child;
            TypeSymbol ts = arg.getType();
            Size lastSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(ts.getTypeDclNode().fullName));
            if (ts.value.equals(JoosNonTerminal.LONG)) {
                pushLong(arg, instructions, sizeHelper);
            } else {
                instructions.add(new Push(Register.ACCUMULATOR, sizeHelper.getPushSize(lastSize), sizeHelper));
            }
            types.add(ts.getTypeDclNode().fullName);
        }

        instructions.add(new Push(Register.BASE, sizeHelper));

        ConstructorSymbol cs = null;
        try {
            cs = resolver.getConstructor(types, resolver);
        } catch (UndeclaredException e) {
            //Should never get here
            e.printStackTrace();
            return;
        }

        Immediate arg = new Immediate(APkgClassResolver.generateFullId(cs));
        if (resolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions.add(new Extern(arg));
        }

        instructions.add(new Call(arg, sizeHelper));
        //return value is the new object
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        long mySize = cs.getStackSize(platform) - sizeHelper.getDefaultStackSize();
        if (mySize != 0) {
            Immediate by = new Immediate(String.valueOf(mySize));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }
        instructions.add(new Comment("Restore Base register"));
        instructions.add(new Pop(Register.BASE, sizeHelper));
    }

    @Override
    public void strPartHelper(ISymbol child, APkgClassResolver resolver,
                              Addable<X86Instruction> instructions,
                              Platform<X86Instruction, Size> platform) {

        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        String firstType = ((Typeable) child).getType().getTypeDclNode().fullName;
        instructions.addAll(platform.getBest(child));
        AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(firstType), false);
        if (ms == null) {
            ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(JoosNonTerminal.OBJECT), false);
        }

        Size lastSize = sizeHelper.getPushSize(sizeHelper.getSize(sizeHelper.getByteSizeOfType(firstType)));
        int pop = sizeHelper.getIntSize(lastSize);

        instructions.add(new Push(Register.ACCUMULATOR, lastSize, sizeHelper));

        Immediate arg = new Immediate(APkgClassResolver.generateFullId(ms));

        if (ms.dclInResolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions.add(new Extern(
                    arg));
        }
        instructions.add(new Call(arg, sizeHelper));

        instructions.add(new Add(Register.STACK, new Immediate(pop), sizeHelper));
    }

    @Override
    public void callStartHelper(SimpleMethodInvoke invoke, Addable<X86Instruction> instructions,
                                Platform<X86Instruction, Size> platform) {

        MethodOrConstructorSymbol call = invoke.call;
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        if (call.isNative()) {
            instructions.add(new Comment("Backing up registers that are to be saved"));
            instructions.add(new Push(Register.BASE, sizeHelper));
            instructions.add(new Push(Register.FRAME, sizeHelper));
            instructions.add(new Push(Register.STACK, sizeHelper));
        }

        instructions.add(new Comment("Pushing args"));

        for (ISymbol isymbol : invoke.children) {
            Typeable arg = (Typeable) isymbol;
            TypeSymbol ts = arg.getType();
            instructions.addAll(platform.getBest(arg));
            Size lastSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(ts.getTypeDclNode().fullName));
            if (ts.value.equals(JoosNonTerminal.LONG)) {
                pushLong(arg, instructions, sizeHelper);
            } else {
                instructions.add(new Push(Register.ACCUMULATOR, sizeHelper.getPushSize(lastSize), sizeHelper));
            }
        }
    }

    @Override
    public void callEndHelper(MethodOrConstructorSymbol call, Addable<X86Instruction> instructions,
                              Platform<X86Instruction, Size> platform) {

        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        // NOTE: do not use INVOKE in here, invoke gets size from method,
        // but visitor may visit InvokeSymbol before MethodSymbol
        long mySize = call.getStackSize(platform);
        if (mySize != 0) {
            Immediate by = new Immediate(mySize);
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }

        if (call.isNative()) {
            instructions.add(new Comment("Restoring up registers that are to be saved"));
            instructions.add(new Pop(Register.STACK, sizeHelper));
            instructions.add(new Pop(Register.FRAME, sizeHelper));
            instructions.add(new Pop(Register.BASE, sizeHelper));
        }

        instructions.add(new Comment("end invoke"));
    }

    @Override
    public void setupJump(String lblTo, SizeHelper<X86Instruction, Size> sizeHelper,
                          Addable<X86Instruction> instructions) {
        instructions.add(new Jmp(new Immediate(lblTo), sizeHelper));
    }

    @Override
    public void setupComment(String comment, Addable<X86Instruction> instructions) {
        instructions.add(new Comment(comment));
    }

    @Override
    public void setupLbl(String lbl, Addable<X86Instruction> instructions) {
        instructions.add(new Label(lbl));
    }

    @Override
    public void setupExtern(String extern, Addable<X86Instruction> instructions) {
        instructions.add(new Extern(extern));
    }

    @Override
    public final void loadBool(boolean bool, Addable<X86Instruction> instructions,
                               SizeHelper<X86Instruction, Size> sizeHelper) {

        instructions.add(new Mov(Register.ACCUMULATOR, bool ? Immediate.TRUE : Immediate.FALSE, sizeHelper));
    }

    @Override
    public void allocateStackSpace(String what, Addable<X86Instruction> instructions, long amount,
                                   Platform<X86Instruction, Size> platform) {
        if (0 != amount) {
            SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
            Immediate by = new Immediate(String.valueOf(amount));
            instructions.add(platform.makeComment("add stack space for " + what));
            instructions.add(new Sub(Register.STACK, by, sizeHelper));
        }
    }

    @Override
    public void cleanStackSpace(String what, Addable<X86Instruction> instructions, long amuont,
                                Platform<X86Instruction, Size> platform) {
        if (0 != amuont) {
            SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
            Immediate by = new Immediate(String.valueOf(amuont));
            instructions.add(platform.makeComment("clean stack space from " + what));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }
    }

    @Override
    public void loadThisToDefault(Addable<X86Instruction> instructions,
                                  SizeHelper<X86Instruction, Size> sizeHelper) {
        instructions.add(new Comment("This (or super) pointer"));
        instructions.add(new Mov(Register.ACCUMULATOR, Memory.getThisPointer(sizeHelper), sizeHelper));
    }

    @Override
    public void makeCall(String to, Addable<X86Instruction> instructions, SizeHelper<X86Instruction
            , Size> sizeHelper) {
        Immediate arg = new Immediate(to);
        instructions.add(new Call(arg, sizeHelper));
    }

    @Override
    public void setupStaticHeader(Addable<X86Instruction> instructions, SizeHelper<X86Instruction, Size> sizeHelper) {
        // Nothing to do
    }

    @Override
    public void setupStaticFooter(Addable<X86Instruction> instructions, SizeHelper<X86Instruction, Size> sizeHelper) {
        // Nothing to do
    }
}
