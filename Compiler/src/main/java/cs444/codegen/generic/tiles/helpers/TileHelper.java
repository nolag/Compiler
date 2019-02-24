package cs444.codegen.generic.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.expressions.BinOpExpr;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.types.APkgClassResolver;

import java.util.List;

public abstract class TileHelper<T extends Instruction<T>, E extends Enum<E>> {
    public static final char NULL = 0;

    public static boolean isZero(ISymbol symbol) {
        if (symbol instanceof INumericLiteral) {
            INumericLiteral numLit = (INumericLiteral) symbol;
            return numLit.getAsLongValue() == 0;
        }
        return false;
    }

    public static boolean isReferenceType(CastExpressionSymbol cast) {
        TypeSymbol type = cast.getType();
        String typeName = type.getTypeDclNode().fullName;
        return !JoosNonTerminal.primativeNumbers.contains(typeName) && !JoosNonTerminal.otherPrimatives.contains(typeName);
    }

    public static Integer powerTwoOrNull(ISymbol symbol, long max, long offset) {
        if (!(symbol instanceof INumericLiteral)) {
            return null;
        }
        INumericLiteral number = (INumericLiteral) symbol;
        long value = number.getAsLongValue();
        if (value < 1) {
            return null;
        }
        value = value - offset;
        if (value > max) {
            return null;
        }
        for (int i = 1; i < 64; i++) {
            if (value == 1 << i) {
                return i;
            }
        }

        return null;
    }

    public static Integer powerTwoOrNull(ISymbol symbol, long max) {
        return powerTwoOrNull(symbol, max, 0);
    }

    public static Integer powerTwoOrNull(ISymbol symbol) {
        return powerTwoOrNull(symbol, Long.MAX_VALUE, 0);
    }

    public static PowerTwoOffset intOffsetFromPow2(ISymbol symbol) {
        if (!(symbol instanceof INumericLiteral)) {
            return null;
        }
        INumericLiteral number = (INumericLiteral) symbol;
        long value = number.getAsLongValue();
        if (value < 1) {
            return null;
        }
        double raw_power = Math.log(value) / Math.log(2);
        long power = (int) raw_power;
        long offset = value - (long) (Math.pow(2, power));
        return new PowerTwoOffset(power, offset);
    }

    public static Long getValue(ISymbol symbol) {
        if (!(symbol instanceof INumericLiteral)) {
            return null;
        }
        INumericLiteral number = (INumericLiteral) symbol;
        return number.getAsLongValue();
    }

    public abstract void setupJmpNull(String ifNullLbl, SizeHelper<T, E> sizeHelper,
                                      Addable<T> instructions);

    public abstract void methProlog(MethodOrConstructorSymbol method, String methodName,
                                    SizeHelper<T, E> sizeHelper,
                                    Addable<T> instructions);

    public abstract void methEpilogue(SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void setupJumpNe(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void setupJumpNeFalse(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void invokeConstructor(APkgClassResolver resolver, List<ISymbol> children,
                                           Platform<T, E> platform,
                                           Addable<T> instructions);

    public abstract void strPartHelper(ISymbol child, APkgClassResolver resolver,
                                       Addable<T> instructions,
                                       Platform<T, E> platform);

    public abstract void callStartHelper(SimpleMethodInvoke invoke, Addable<T> instructions,
                                         Platform<T, E> platform);

    public abstract void callEndHelper(MethodOrConstructorSymbol call, Addable<T> instructions,
                                       Platform<T, E> platform);

    public abstract void setupJump(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void setupComment(String comment, Addable<T> instructions);

    public abstract void setupLbl(String lbl, Addable<T> instructions);

    public abstract void setupExtern(String extern, Addable<T> instructions);

    public abstract void loadBool(boolean bool, Addable<T> instructions, SizeHelper<T, E> sizeHelper);

    public abstract void makeLong(Typeable item, Addable<T> instructions,
                                  SizeHelper<T, E> sizeHelper);

    public abstract void pushLong(Typeable item, Addable<T> instructions,
                                  SizeHelper<T, E> sizeHelper);

    public abstract void allocateStackSpace(String what, Addable<T> instructions, long amount,
                                            Platform<T, E> platform);

    public abstract void cleanStackSpace(String what, Addable<T> instructions, long amount,
                                         Platform<T, E> platform);

    public abstract void loadThisToDefault(Addable<T> instructions, SizeHelper<T, E> sizeHelper);

    public abstract void loadNumberToDefault(INumericLiteral numeric, Addable<T> instructions,
                                             SizeHelper<T, E> sizeHelper);

    public void loadNumberToDefault(int numeric, Addable<T> instructions,
                                    SizeHelper<T, E> sizeHelper) {
        loadNumberToDefault(new IntegerLiteralSymbol(numeric), instructions, sizeHelper);
    }

    public abstract void makeCall(String to, Addable<T> instructions, SizeHelper<T, E> sizeHelper);

    public boolean fitsSizedCompare(BinOpExpr op, Platform<T, E> platform) {
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        Typeable ts1 = (Typeable) op.children.get(0);
        Typeable ts2 = (Typeable) op.children.get(1);
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(ts1.getType().getTypeDclNode().fullName)
                && sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(ts2.getType().getTypeDclNode().fullName);
    }

    public abstract void setupStaticHeader(Addable<T> instructions, SizeHelper<T, E> sizeHelper);

    public abstract void setupStaticFooter(Addable<T> instructions, SizeHelper<T, E> sizeHelper);

    public static class PowerTwoOffset {
        public final long power;
        public final long offset;

        private PowerTwoOffset(long power, long offset) {
            this.power = power;
            this.offset = offset;
        }
    }
}
