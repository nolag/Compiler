package cs444.parser.symbols.ast.cleanup.factories;

import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.factories.ASTSymbolFactory;
import cs444.types.exceptions.StaticToNonStaticConversion;

public class LookupLinkCleanFacory extends ASTSymbolFactory {
    @Override
    protected ISymbol convert(final ISymbol from) throws StaticToNonStaticConversion{
        if(from instanceof MethodInvokeSymbol){
            final MethodInvokeSymbol invoke = (MethodInvokeSymbol) from;
            Typeable latest = new SimpleMethodInvoke(invoke);
            final ISymbol lookup = invoke.getLookup().convert();
            final TypeSymbol typeSymbol = latest.getType();
            if(invoke.hasFirst) latest = new FieldAccessSymbol(convert(invoke.children.get(0)), latest, typeSymbol);
            else if(lookup != null) latest = new FieldAccessSymbol(lookup, latest, typeSymbol);
            return latest;
        }else if(from instanceof NameSymbol){
            return ((NameSymbol) from).getDclNode().convert();
        }
        return from;
    }
}
