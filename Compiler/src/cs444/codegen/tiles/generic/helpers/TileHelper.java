package cs444.codegen.tiles.generic.helpers;

import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
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
}
