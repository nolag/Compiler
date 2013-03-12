package cs444.types;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.types.exceptions.DuplicateDeclarationException;
import cs444.types.exceptions.ImplicitStaticConversionException;
import cs444.types.exceptions.UndeclaredException;

public class LocalDclLinker extends EmptyVisitor {
    private LocalScope currentScope;
    private final String enclosingClassName; // used for exception message

    private final Stack<Deque<Typeable>> currentTypes = new Stack<Deque<Typeable>>();
    private final Stack<Boolean> useCurrentForLookup = new Stack<Boolean>();

    public LocalDclLinker(String enclosingClassName){
        this.enclosingClassName = enclosingClassName;
        currentTypes.add(new ArrayDeque<Typeable>());
        useCurrentForLookup.add(false);
    }

    // creates root scope which will contain parameters declarations
    @Override
    public void open(MethodOrConstructorSymbol methodSymbol){
        pushNewScope(methodSymbol.isStatic());
    }

    @Override
    public void close(MethodOrConstructorSymbol methodSymbol){
        popCurrentScope();
    }

    @Override
    public void close(DclSymbol dclSymbol) throws CompilerException {
        // in close because we cannot used this variable inside its initializer
        String varName = dclSymbol.dclName;
        if (currentScope.isDeclared(varName)) throw new DuplicateDeclarationException(varName, enclosingClassName);
        currentScope.add(varName, dclSymbol);
    }

    @Override
    public void open(NonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            pushNewScope(currentScope.isStatic);
        }
    }

    @Override
    public void close(NonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            popCurrentScope();
        }
    }

    @Override
    public void open(MethodInvokeSymbol invoke) throws CompilerException {

        Deque<Typeable> currentSymbols = currentTypes.pop();

        APkgClassResolver resolver = currentSymbols.isEmpty() ?
                PkgClassInfo.instance.getSymbol(enclosingClassName) : currentSymbols.peek().getType().getTypeDclNode();

        boolean isStatic = invoke.children.isEmpty() ? currentScope.isStatic : false;
        if(invoke.lookupFirst != null){
            DclSymbol dcl = null;
            if(invoke.children.isEmpty()) dcl = currentScope.find(invoke.lookupFirst);
            if(dcl == null)dcl = resolver.getDcl(invoke.lookupFirst, isStatic, resolver);
            currentSymbols.add(dcl);
            resolver = dcl.type.getTypeDclNode();
            //TODO it is possible that the above was not static but this is, this is a bigger issue in lookup dcl maybe?
            isStatic = dcl.type.isClass;
        }

        LookupLink lookup = new LookupLink(new LinkedList<Typeable>(currentSymbols));
        invoke.setLookup(lookup);
        useCurrentForLookup.pop();
        useCurrentForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(MethodInvokeSymbol invoke) throws CompilerException {
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(enclosingClassName);
        boolean isStatic = currentScope.isStatic;
        Deque<Typeable>currentSymbols = currentTypes.pop();

        LookupLink lookup = invoke.getLookup();

        if(lookup.lastDcl != null){
            TypeSymbol type = lookup.getType();
            resolver = type.getTypeDclNode();
            isStatic = type.isClass;
        }

        List<String> params = new LinkedList<String>();

        for(Typeable mod : currentSymbols){
            params.add(0, mod.getType().getTypeDclNode().fullName);
        }

//        TODO: bring these  one back
        // AMethodSymbol method = resolver.findMethod(invoke.methodName, isStatic, params);
       // invoke.setLookup(invoke.getLookup().addWith(method));
       // currentTypes.peek().add(method);
    }

    private void pushNewScope(boolean isStatic) {
        currentScope = new LocalScope(currentScope, isStatic);
    }

    private void popCurrentScope() {
        currentScope = currentScope.parent;
    }

    @Override
    public void visit(NameSymbol nameSymbol) throws UndeclaredException, ImplicitStaticConversionException{
        String [] lookupNames = nameSymbol.value.split("\\.");

        DclSymbol dclNode = null;

        if(!useCurrentForLookup.peek()) dclNode = currentScope.find(lookupNames[0]);
        LookupLink link;

        if(dclNode != null){
            List<Typeable> dclList;
            if(lookupNames.length == 1){
                dclList = Arrays.asList(new Typeable [] {dclNode});

            }else{
                APkgClassResolver resolver = dclNode.type.getTypeDclNode();
                List<DclSymbol> restList = resolver.findDcl(nameSymbol.value.substring(lookupNames[0].length() + 1), false);
                dclList = new LinkedList<Typeable>(restList);
                dclList.add(0, dclNode);

            }
            link = new LookupLink(dclList);
        }else{
            APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(enclosingClassName);
            boolean isStatic = currentScope.isStatic;
            Typeable currentSymbol = useCurrentForLookup.peek() ? currentTypes.peek().getLast() : null;

            if(currentSymbol != null){
                resolver = currentSymbol.getType().getTypeDclNode();
                isStatic = currentSymbol.getType().isClass;
            }

            List<Typeable> modOp = new LinkedList<Typeable>(resolver.findDcl(nameSymbol.value, isStatic));
            link = new LookupLink(modOp);
        }

        nameSymbol.setDclNode(link);
        currentTypes.peek().add(nameSymbol);
    }

    @Override
    public void prepare(MethodInvokeSymbol invoke) throws CompilerException {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentForLookup.push(true);
    }
}
