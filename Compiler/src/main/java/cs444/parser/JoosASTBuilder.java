package cs444.parser;

import cs444.CompilerException;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.factories.*;
import cs444.parser.symbols.ast.factories.expressions.*;
import cs444.types.exceptions.InvalidFileNameException;

import java.util.Arrays;

public class JoosASTBuilder implements IASTBuilder {
    public static final Iterable<ASTSymbolFactory> simplifications = Arrays.asList(new ListedSymbolFactory(),
            new OneChildFactory(), new NameSymbolFactory(), new TypeSymbolFactory(),
            new ClassInterfaceFactory(), new FieldSymbolFactory(), new MethodSymbolFactory(),
            new FieldAccessSymbolFactory(),
            new LocalDclFactory(), new InterfaceMethodSymbolFactory(), new ArrayAccessFactory(),
            new ConstructorSymbolFactory(),
            new MethodInvokeSymbolFactory(), new IntegerLiteralFactory(), new StringLiteralFactory(),
            new SimpleTerminalFactory(),
            new ReturnExprFractory(), new UniOpExprFactory(), new BinOpFactory(), new CreationExprFactory(),
            new CastExpressionFactory(),
            new IfExprFactory(), new WhileExprFactory(), new ForExprFactory());

    private final String fileName;

    public JoosASTBuilder(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ISymbol build(ANonTerminal start) throws CompilerException {
        for (ASTSymbolFactory astSymbol : simplifications) {
            start = (ANonTerminal) astSymbol.convertAll(start);
        }

        AInterfaceOrClassSymbol publicClassInterface = (AInterfaceOrClassSymbol) start;
        if (!fileName.equals(publicClassInterface.dclName + ".java")) {
            throw new InvalidFileNameException(publicClassInterface.dclName, fileName);
        }

        return start;
    }
}
