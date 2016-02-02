package cs444.codegen.generic.tiles.helpers;

import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.expressions.BinOpExpr;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.types.APkgClassResolver;

public abstract class TileHelper<T extends Instruction<T>, E extends Enum<E>> {
    public static final char NULL = 0;

    public static boolean isZero(final ISymbol symbol) {
        if (symbol instanceof INumericLiteral) {
            final INumericLiteral numLit = (INumericLiteral) symbol;
            return numLit.getAsLongValue() == 0;
        }
        return false;
    }

    public static boolean isReferenceType(final CastExpressionSymbol cast) {
        final TypeSymbol type = cast.getType();
        final String typeName = type.getTypeDclNode().fullName;
        return !JoosNonTerminal.primativeNumbers.contains(typeName) && !JoosNonTerminal.otherPrimatives.contains(typeName);
    }

    public abstract void setupJmpNull(final String ifNullLbl, final SizeHelper<T, E> sizeHelper, final Addable<T> instructions);

    public abstract void methProlog(final MethodOrConstructorSymbol method, final String methodName, final SizeHelper<T, E> sizeHelper,
            final Addable<T> instructions);

    public abstract void methEpilogue(final SizeHelper<T, E> sizeHelper, final Addable<T> instructions);

    public abstract void setupJumpNe(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void setupJumpNeFalse(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void invokeConstructor(final APkgClassResolver resolver, final List<ISymbol> children, final Platform<T, E> platform,
            final Addable<T> instructions);

    public abstract void strPartHelper(final ISymbol child, final APkgClassResolver resolver, final Addable<T> instructions,
            final Platform<T, E> platform);

    public abstract void callStartHelper(final SimpleMethodInvoke invoke, final Addable<T> instructions, final Platform<T, E> platform);

    public abstract void callEndHelper(final MethodOrConstructorSymbol call, final Addable<T> instructions, final Platform<T, E> platform);

    public abstract void setupJump(final String lblTo, SizeHelper<T, E> sizeHelper, final Addable<T> instructions);

    public abstract void setupComment(final String comment, final Addable<T> instructions);

    public abstract void setupLbl(final String lbl, final Addable<T> instructions);

    public abstract void setupExtern(final String extern, final Addable<T> instructions);

    public abstract void loadBool(final boolean bool, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public abstract void makeLong(final Typeable item, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public abstract void pushLong(final Typeable item, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public abstract void allocateStackSpace(final String what, final Addable<T> instructions, final long amount,
            final Platform<T, E> platform);

    public abstract void cleanStackSpace(final String what, final Addable<T> instructions, final long amount, final Platform<T, E> platform);

    public static Integer powerTwoOrNull(final ISymbol symbol, final long max, final long offset) {
        if (!(symbol instanceof INumericLiteral)) return null;
        final INumericLiteral number = (INumericLiteral) symbol;
        long value = number.getAsLongValue();
        if (value < 1) return null;
        value = value - offset;
        if (value > max) return null;
        int power = 0;
        int shift = 1;
        for (int i = 1; i < 64; i++) {
            if ((value | shift) != 0) {
                if (power != 0) return null;
                power = i;
                shift <<= 1;
            }
        }

        return power;
    }

    public static Integer powerTwoOrNull(final ISymbol symbol, final long max) {
        return powerTwoOrNull(symbol, max, 0);
    }

    public static Integer powerTwoOrNull(final ISymbol symbol) {
        return powerTwoOrNull(symbol, Long.MAX_VALUE, 0);
    }

    public static PowerTwoOffset intOffsetFromPow2(final ISymbol symbol) {
        if (!(symbol instanceof INumericLiteral)) return null;
        final INumericLiteral number = (INumericLiteral) symbol;
        final long value = number.getAsLongValue();
        if (value < 1) return null;
        final double raw_power = Math.log(value) / Math.log(2);
        final long power = (int) raw_power;
        final long offset = value - (long) (Math.pow(2, power));
        return new PowerTwoOffset(power, offset);
    }

    public static class PowerTwoOffset {
        public final long power;
        public final long offset;

        private PowerTwoOffset(final long power, final long offset) {
            this.power = power;
            this.offset = offset;
        }
    }

    public static Long getValue(final ISymbol symbol) {
        if (!(symbol instanceof INumericLiteral)) return null;
        final INumericLiteral number = (INumericLiteral) symbol;
        return number.getAsLongValue();
    }

    public abstract void loadThisToDefault(final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public abstract void loadNumberToDefault(final INumericLiteral numeric, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public void loadNumberToDefault(final int numeric, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper) {
        loadNumberToDefault(new IntegerLiteralSymbol(numeric), instructions, sizeHelper);
    }

    public abstract void makeCall(final String to, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public boolean fitsSizedCompare(final BinOpExpr op, final Platform<T, E> platform) {
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final Typeable ts1 = (Typeable) op.children.get(0);
        final Typeable ts2 = (Typeable) op.children.get(1);
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(ts1.getType().getTypeDclNode().fullName)
                && sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(ts2.getType().getTypeDclNode().fullName);
    }

    public abstract void setupStaticHeader(final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public abstract void setupStaticFooter(final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);
}
