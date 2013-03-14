package cs444.parser;

import java.util.Arrays;

import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.parser.symbols.ast.factories.ArrayAccessFactory;
import cs444.parser.symbols.ast.factories.ClassInterfaceFactory;
import cs444.parser.symbols.ast.factories.ConstructorSymbolFactory;
import cs444.parser.symbols.ast.factories.FieldAccessSymbolFactory;
import cs444.parser.symbols.ast.factories.FieldSymbolFactory;
import cs444.parser.symbols.ast.factories.IntegerLiteralFactory;
import cs444.parser.symbols.ast.factories.InterfaceMethodSymbolFactory;
import cs444.parser.symbols.ast.factories.ListedSymbolFactory;
import cs444.parser.symbols.ast.factories.LocalDclFactory;
import cs444.parser.symbols.ast.factories.MethodInvokeSymbolFactory;
import cs444.parser.symbols.ast.factories.MethodSymbolFactory;
import cs444.parser.symbols.ast.factories.NameSymbolFactory;
import cs444.parser.symbols.ast.factories.OneChildFactory;
import cs444.parser.symbols.ast.factories.SimpleTerminalFactory;
import cs444.parser.symbols.ast.factories.StringLiteralFactory;
import cs444.parser.symbols.ast.factories.TypeSymbolFactory;
import cs444.parser.symbols.ast.factories.expressions.BinOpFactory;
import cs444.parser.symbols.ast.factories.expressions.CastExpressionFactory;
import cs444.parser.symbols.ast.factories.expressions.CreationExprFactory;
import cs444.parser.symbols.ast.factories.expressions.ForExprFactory;
import cs444.parser.symbols.ast.factories.expressions.IfExprFactory;
import cs444.parser.symbols.ast.factories.expressions.ReturnExprFractory;
import cs444.parser.symbols.ast.factories.expressions.UniOpExprFactory;
import cs444.parser.symbols.ast.factories.expressions.WhileExprFactory;
import cs444.parser.symbols.exceptions.IllegalModifierException;
import cs444.parser.symbols.exceptions.OutOfRangeException;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.exceptions.InvalidFileNameException;

public class JoosASTBuilder implements IASTBuilder {
    public static final Iterable<ASTSymbolFactory> simplifications = Arrays.asList(new ASTSymbolFactory [] {
            new ListedSymbolFactory(), new OneChildFactory(), new NameSymbolFactory(), new TypeSymbolFactory(),
            new ClassInterfaceFactory(), new FieldSymbolFactory(), new MethodSymbolFactory(), new FieldAccessSymbolFactory(),
            new LocalDclFactory(), new InterfaceMethodSymbolFactory(), new ArrayAccessFactory(), new ConstructorSymbolFactory(),
            new MethodInvokeSymbolFactory(), new IntegerLiteralFactory(), new StringLiteralFactory(), new SimpleTerminalFactory(),
            new ReturnExprFractory(), new UniOpExprFactory(), new BinOpFactory(), new CreationExprFactory(), new CastExpressionFactory(),
            new IfExprFactory(), new WhileExprFactory(), new ForExprFactory()});

    public ISymbol build(String fileName, ANonTerminal start) throws OutOfRangeException, UnsupportedException, IllegalModifierException, InvalidFileNameException {
    	for(ASTSymbolFactory astSymbol : simplifications){
    		start = (ANonTerminal)astSymbol.convertAll(start);
    	}

    	AInterfaceOrClassSymbol publicClassInterface = (AInterfaceOrClassSymbol) start;
    	if (!fileName.equals(publicClassInterface.dclName + ".java"))
    		throw new InvalidFileNameException(publicClassInterface.dclName, fileName);

    	return start;
    }
}
