package cs444.types;

import cs444.ast.EmptyVisitor;
import cs444.parser.symbols.ast.TypeSymbol;

public class TypeResolverVisitor extends EmptyVisitor {

    @Override
    public void visit(TypeSymbol typeSymbol) {
        // TODO: resolve this type using typeSymbol.value()
    }
}
