package cs444.codegen.x86.tiles.helpers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.*;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

public class TileHelper {
    public static void genMov(final Size size, final InstructionArg from, final String value,
            final Typeable dcl, final X86SizeHelper sizeHelper, final Addable<X86Instruction> instructions){


        X86Instruction instruction;

        if(size == Size.DWORD){
            instruction = new Mov(Register.ACCUMULATOR, from, sizeHelper);
        }else if(JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)){
            instruction = new Movzx(Register.ACCUMULATOR, from, size, sizeHelper);
        }else{
            instruction = new Movsx(Register.ACCUMULATOR, from, size, sizeHelper);
        }

        instructions.add(new Comment("getting value of " + value));
        instructions.add(instruction);
    }

    public static void ifNullJmpCode(final Register register, final String ifNullLbl,
            final X86SizeHelper sizeHelper, final Addable<X86Instruction> instructions) {
        instructions.add(new Comment("null check"));
        instructions.add(new Cmp(register, Immediate.NULL, sizeHelper));
        instructions.add(new Je(new Immediate(ifNullLbl), sizeHelper));
    }

    public static void methProlog(final MethodOrConstructorSymbol method,
            final String methodName, final X86SizeHelper sizeHelper, final Addable<X86Instruction> instructions){
        if(method.getProtectionLevel() != ProtectionLevel.PRIVATE) instructions.add(new Global(methodName));
        instructions.add(new Label(methodName));
        instructions.add(Push.getStackPush(sizeHelper));
        instructions.add(new Mov(Register.FRAME, Register.STACK, sizeHelper));
    }

    public static void methEpilogue(final MethodOrConstructorSymbol method, final Addable<X86Instruction> instructions) {
        //Don't fall though void funcs
        instructions.add(Leave.LEAVE);
        instructions.add(Ret.RET);

        instructions.add(new Comment("End of method " + method.dclName));
    }

    public static void setupJumpNe(final Register reg, final Immediate when,
            final String lblTo, final X86SizeHelper sizeHelper, final Addable<X86Instruction> instructions){
        instructions.add(new Cmp(reg, when, sizeHelper));
        instructions.add(new Jne(new Immediate(lblTo), sizeHelper));
    }

    public static void invokeConstructor(final APkgClassResolver resolver, final List<ISymbol> children,
            final Platform<X86Instruction, X86SizeHelper> platform, final Addable<X86Instruction> instructions) {

        final List<String> types = new LinkedList<String>();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Back up addr of obj in Base so it is safe"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        for(final ISymbol child : children){
            instructions.addAll(platform.getBest(child));
            final Typeable arg = (Typeable) child;
            final Size lastSize =  X86SizeHelper.getPushSize(sizeHelper.getSizeOfType(arg.getType().getTypeDclNode().fullName));
            instructions.add(new Push(Register.ACCUMULATOR, X86SizeHelper.getPushSize(lastSize), sizeHelper));
            final Typeable typeable = (Typeable) child;
            final TypeSymbol ts = typeable.getType();
            types.add(ts.getTypeDclNode().fullName);
        }

        instructions.add(new Push(Register.BASE, sizeHelper));

        ConstructorSymbol cs = null;
        try {
            cs = resolver.getConstructor(types, resolver);
        } catch (final UndeclaredException e) {
            //Should never get here
            e.printStackTrace();
        }

        final Immediate arg = new Immediate(APkgClassResolver.generateFullId(cs));
        if(resolver != CodeGenVisitor.getCurrentCodeGen().currentFile) instructions.add(new Extern(arg));

        instructions.add(new Call(arg, sizeHelper));
        //return value is the new object
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        final long mySize = cs.getStackSize() - sizeHelper.getDefaultStackSize();
        if(mySize != 0){
            final Immediate by = new Immediate(String.valueOf(mySize));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }
        instructions.add(new Comment("Restore Base register"));
        instructions.add(new Pop(Register.BASE, sizeHelper));
    }

    public static void strPartHelper(final ISymbol child, final APkgClassResolver resolver,
            final Addable<X86Instruction> instructions, final Platform<X86Instruction, X86SizeHelper> platform){

        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final String firstType = ((Typeable)child).getType().getTypeDclNode().fullName;
        instructions.addAll(platform.getBest(child));
        AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(firstType), false);
        if(ms == null) ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(JoosNonTerminal.OBJECT), false);



        final Size lastSize = X86SizeHelper.getPushSize(X86SizeHelper.getSize(sizeHelper.getByteSizeOfType(firstType)));
        final int pop = X86SizeHelper.getIntSize(lastSize);

        instructions.add(new Push(Register.ACCUMULATOR, lastSize, sizeHelper));

        final Immediate arg = new Immediate(APkgClassResolver.generateFullId(ms));
        if(ms.dclInResolver != CodeGenVisitor.getCurrentCodeGen().currentFile) instructions.add(new Extern(arg));
        instructions.add(new Call(arg, sizeHelper));

        instructions.add(new Add(Register.STACK, new Immediate(pop), sizeHelper));
    }

    public static void callStartHelper(final SimpleMethodInvoke invoke,
            final Addable<X86Instruction> instructions, final Platform<X86Instruction, X86SizeHelper> platform){
        instructions.add(new Comment("Pushing args"));

        final X86SizeHelper sizeHelper = platform.getSizeHelper();

        for(final ISymbol isymbol : invoke.children){
            final Typeable arg = (Typeable) isymbol;
            instructions.addAll(platform.getBest(arg));
            final Size lastSize =  X86SizeHelper.getPushSize(sizeHelper.getSizeOfType(arg.getType().getTypeDclNode().fullName));
            instructions.add(new Push(Register.ACCUMULATOR, X86SizeHelper.getPushSize(lastSize), sizeHelper));
        }
    }

    public static void callEndHelper(final MethodOrConstructorSymbol call,
            final Addable<X86Instruction> instructions, final Platform<X86Instruction, X86SizeHelper> platform){

        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        // NOTE: do not use INVOKE in here, invoke gets size from method,
        // but visitor may visit InvokeSymbol before MethodSymbol
        final long mySize =  call.getStackSize();
        if(mySize != 0){
            final Immediate by = new Immediate(mySize);
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }
        instructions.add(new Comment("end invoke"));
    }

    public static boolean isReferenceType(final CastExpressionSymbol cast) {
        final TypeSymbol type = cast.getType();
        final String typeName = type.getTypeDclNode().fullName;
        return !JoosNonTerminal.primativeNumbers.contains(typeName) && !JoosNonTerminal.otherPrimatives.contains(typeName);
    }
}
