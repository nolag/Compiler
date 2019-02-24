package cs444.types;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.parser.symbols.ast.TypeSymbol;

public class TypeResolverVisitor extends EmptyVisitor {

    private final PkgClassResolver classResolver;

    public TypeResolverVisitor(PkgClassResolver pkgClassResolver) {
        classResolver = pkgClassResolver;
    }

    @Override
    public void visit(TypeSymbol typeSymbol) throws CompilerException {
        if (typeSymbol.getTypeDclNode() == null && typeSymbol.value != "void") {
            APkgClassResolver resolver = classResolver.findClass(typeSymbol.value);
            typeSymbol.setTypeDclNode(typeSymbol.isArray ? resolver.getArrayVersion() : resolver);
        }
    }
}
