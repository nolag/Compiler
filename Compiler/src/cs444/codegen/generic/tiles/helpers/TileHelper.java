package cs444.codegen.generic.tiles.helpers;

import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.types.APkgClassResolver;

public abstract class TileHelper<T extends Instruction, E extends Enum<E>> {
    public static boolean isZero(final ISymbol symbol){
        if(symbol instanceof INumericLiteral){
            final INumericLiteral numLit = (INumericLiteral) symbol;
            return numLit.getValue() == 0;
        }
        return false;
    }

    public static boolean isReferenceType(final CastExpressionSymbol cast) {
        final TypeSymbol type = cast.getType();
        final String typeName = type.getTypeDclNode().fullName;
        return !JoosNonTerminal.primativeNumbers.contains(typeName) && !JoosNonTerminal.otherPrimatives.contains(typeName);
    }

    public abstract void ifNullJmpCode(final String ifNullLbl, final SizeHelper<T, E> sizeHelper, final Addable<T> instructions);

    public abstract void methProlog(final MethodOrConstructorSymbol method, final String methodName,
            final SizeHelper<T, E> sizeHelper, final Addable<T> instructions);

    public abstract void methEpilogue(final MethodOrConstructorSymbol method, final Addable<T> instructions);

    public abstract void setupJumpNe(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void setupJumpNeFalse(String lblTo, SizeHelper<T, E> sizeHelper, Addable<T> instructions);

    public abstract void invokeConstructor(final APkgClassResolver resolver, final List<ISymbol> children,
            final Platform<T, E> platform, final Addable<T> instructions);

    public abstract void strPartHelper(final ISymbol child, final APkgClassResolver resolver,
            final Addable<T> instructions, final Platform<T, E> platform);

    public abstract void callStartHelper(final SimpleMethodInvoke invoke, final Addable<T> instructions, final Platform<T, E> platform);

    public abstract void callEndHelper(final MethodOrConstructorSymbol call, final Addable<T> instructions, final Platform<T, E> platform);

    public abstract void setupJump(final String lblTo, SizeHelper<T, E> sizeHelper, final  Addable<T> instructions);

    public abstract void setupComment(final String comment, final Addable<T> instructions);

    public abstract void setupLbl(final String lbl, final Addable<T> instructions);

    public abstract void setupExtern(final String extern, final Addable<T> instructions);

    public abstract void loadBool(final boolean bool, final Addable<T> instructions, final SizeHelper<T, E> sizeHelper);

    public abstract void makeLong(final Typeable item,
            final Addable<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper);

    public abstract void pushLong(final Typeable item,
            final Addable<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper);

    public static Integer powerTwoOrNull(final ISymbol symbol, final long max, final long offset) {
        if (!(symbol instanceof INumericLiteral)) return null;
        final INumericLiteral number = (INumericLiteral) symbol;
        long value = number.getValue();
        if (value < 1) return null;
        value = value - offset;
        if (value > max) return null;
        final double power = Math.log(value) / Math.log(2);
        return  power == Math.round(power) ? (int)power : null;
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
        final long value = number.getValue();
        if (value < 1) return null;
        final double raw_power = Math.log(value) / Math.log(2);
        final long power = (int) raw_power;
        final long offset = value - (long)(Math.pow(2, power));
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
        return number.getValue();
    }
}
