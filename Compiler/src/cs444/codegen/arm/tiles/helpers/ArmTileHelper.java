package cs444.codegen.arm.tiles.helpers;

import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate16;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Global;
import cs444.codegen.arm.instructions.Label;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Movt;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.APkgClassResolver;

public class ArmTileHelper extends TileHelper<ArmInstruction, Size> {
    private static final Push ENTER = new Push(Register.INTRA_PROCEDURE, Register.LINK);
    private static final Pop LEAVE = new Pop(Register.INTRA_PROCEDURE, Register.PC);

    private static final Push NATIVE_ENTER = new Push(Register.R8, Register.R9, Register.R10, Register.R11);
    private static final Pop NATIVE_LEAVE = new Pop(Register.R8, Register.R9, Register.R10, Register.R11);

    @Override
    public void ifNullJmpCode(String ifNullLbl, SizeHelper<ArmInstruction, Size> sizeHelper, Addable<ArmInstruction> instructions) {

    }

    @Override
    public void methProlog(MethodOrConstructorSymbol method, String methodName, SizeHelper<ArmInstruction, Size> sizeHelper,
            Addable<ArmInstruction> instructions) {
        if (method.getProtectionLevel() != ProtectionLevel.PRIVATE) instructions.add(new Global(methodName));
        instructions.add(new Label(methodName));
        instructions.add(ENTER);
    }

    @Override
    public void methEpilogue(MethodOrConstructorSymbol method, Addable<ArmInstruction> instructions) {
        instructions.add(LEAVE);
        instructions.add(new Comment("End of method " + method.dclName));
    }

    @Override
    public void setupJumpNe(String lblTo, SizeHelper<ArmInstruction, Size> sizeHelper, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setupJumpNeFalse(String lblTo, SizeHelper<ArmInstruction, Size> sizeHelper, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void invokeConstructor(APkgClassResolver resolver, List<ISymbol> children, Platform<ArmInstruction, Size> platform,
            Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void strPartHelper(ISymbol child, APkgClassResolver resolver, Addable<ArmInstruction> instructions,
            Platform<ArmInstruction, Size> platform) {
        // TODO Auto-generated method stub

    }

    @Override
    public void callStartHelper(SimpleMethodInvoke invoke, Addable<ArmInstruction> instructions, Platform<ArmInstruction, Size> platform) {
        final MethodOrConstructorSymbol call = invoke.call;
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        if (call.isNative()) {
            instructions.add(new Comment("Backing up registers that are to be saved"));
            instructions.add(NATIVE_ENTER);
        }

        instructions.add(new Comment("Pushing args"));

        for (final ISymbol isymbol : invoke.children) {
            final Typeable arg = (Typeable) isymbol;
            final TypeSymbol ts = arg.getType();
            instructions.addAll(platform.getBest(arg));
            //I don't see a way to push 2 bytes...
            if (ts.value.equals(JoosNonTerminal.LONG)) pushLong(arg, instructions, sizeHelper);
            else instructions.add(new Push(Register.R0));
        }
    }

    @Override
    public void callEndHelper(MethodOrConstructorSymbol call, Addable<ArmInstruction> instructions, Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        // NOTE: do not use INVOKE in here, invoke gets size from method,
        // but visitor may visit InvokeSymbol before MethodSymbol
        final long mySize = call.getStackSize(platform);
        if (mySize != 0) {
            instructions.add(new Add(Register.STACK, Register.STACK, setupOp2(Register.R1, mySize, instructions, sizeHelper), sizeHelper));
        }

        if (call.isNative()) {
            instructions.add(new Comment("Restoring up registers that are to be saved"));
            instructions.add(NATIVE_LEAVE);
        }

        instructions.add(new Comment("end invoke"));

    }

    @Override
    public void setupJump(String lblTo, SizeHelper<ArmInstruction, Size> sizeHelper, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setupComment(String comment, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setupLbl(String lbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setupExtern(String extern, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadBool(boolean bool, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        // TODO Auto-generated method stub

    }

    @Override
    public void makeLong(Typeable item, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pushLong(Typeable item, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        // TODO Auto-generated method stub
    }

    public static Operand2 setupOp2(Register pref, long n, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        if (n >= 0) {
            if (n <= 255) { return new Immediate8((char) n); }
            //TODO other number optimizations that are shifts
        }

        if (n > Integer.MAX_VALUE || n < Integer.MIN_VALUE) {
            //TODO long literal that must be long
            throw new NumberFormatException(n + " is too big or small for now :(");
        }
        int ival = (int) n;
        instructions.add(new Mov(pref, new Immediate16(ival & 0xFFFF), sizeHelper));
        instructions.add(new Movt(pref, new Immediate16(ival & 0xFFFF0000 >> 16), sizeHelper));
        return pref;
    }
}
