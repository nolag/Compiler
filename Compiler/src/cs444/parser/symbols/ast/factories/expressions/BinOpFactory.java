package cs444.parser.symbols.ast.factories.expressions;

import cs444.lexer.Token;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;

public class BinOpFactory extends ASTSymbolFactory{

    @Override
    protected ISymbol convert(ISymbol from) throws OutOfRangeException, UnsupportedException, IllegalModifierException {
        if(!JoosNonTerminal.binExpressions.contains(from.getName().toUpperCase())) return from;

        ANonTerminal nonTerm = (ANonTerminal) from;
        ISymbol left = nonTerm.children.get(0);
        ISymbol symbol = nonTerm.children.get(1);
        ISymbol right = nonTerm.children.get(2);
        final String typeName = symbol.getName().toUpperCase();

        if(typeName.equals(Token.Type.STAR.toString())) return new MultiplyExprSymbol(left, right);
        if(typeName.equals(Token.Type.SLASH.toString())) return new DivideExprSymbol(left, right);
        if(typeName.equals(Token.Type.PCT.toString())) return new RemainderExprSymbol(left, right);
        if(typeName.equals(Token.Type.PLUS.toString())) return new AddExprSymbol(left, right);
        if(typeName.equals(Token.Type.MINUS.toString())) return new SubtractExprSymbol(left, right);
        if(typeName.equals(Token.Type.LT.toString())) return new LtExprSymbol(left, right);
        if(typeName.equals(Token.Type.GT.toString())) return new LtExprSymbol(right, left);
        if(typeName.equals(Token.Type.LE.toString())) return new LeExprSymbol(left, right);
        if(typeName.equals(Token.Type.GE.toString())) return new LeExprSymbol(right, left);
        if(typeName.equals(Token.Type.INSTANCEOF.toString())) return new InstanceOfExprSymbol(left, right);
        if(typeName.equals(Token.Type.EQ.toString())) return new EqExprSymbol(left, right);
        if(typeName.equals(Token.Type.NE.toString())) return new NeExprSymbol(left, right);
        if(typeName.equals(Token.Type.DAMPERSAND.toString())) return new AndExprSymbol(left, right);
        if(typeName.equals(Token.Type.DPIPE.toString())) return new OrExprSymbol(left, right);
        if(typeName.equals(Token.Type.AMPERSAND.toString())) return new EAndExprSymbol(left, right);
        if(typeName.equals(Token.Type.PIPE.toString())) return new EOrExprSymbol(left, right);
        if(typeName.equals(Token.Type.BECOMES.toString())) return new AssignmentExprSymbol(left, right);

        throw new UnsupportedException("Binary operator " + typeName);
    }

}
