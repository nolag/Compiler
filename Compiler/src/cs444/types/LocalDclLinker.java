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
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.SuperSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.TypeableTerminal;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.types.exceptions.DuplicateDeclarationException;
import cs444.types.exceptions.IllegalCastAssignmentException;
import cs444.types.exceptions.ImplicitStaticConversionException;
import cs444.types.exceptions.UndeclaredException;

public class LocalDclLinker extends EmptyVisitor {
    private LocalScope currentScope;
    private MethodOrConstructorSymbol currentMC;
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
        currentMC = methodSymbol;
    }

    @Override
    public void close(MethodOrConstructorSymbol methodSymbol){
        popCurrentScope();
        currentMC = null;
    }

    @Override
    public void close(DclSymbol dclSymbol) throws DuplicateDeclarationException {
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
    public void open(MethodInvokeSymbol invoke) throws UndeclaredException, ImplicitStaticConversionException {

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
    public void close(MethodInvokeSymbol invoke) {
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
    public void prepare(MethodInvokeSymbol invoke) {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentForLookup.push(true);
    }

    @Override
    public void open(FieldAccessSymbol access){
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentForLookup.push(true);
    }

    @Override
    public void close(FieldAccessSymbol access){
        currentTypes.pop();
        useCurrentForLookup.pop();
    }

    private void simpleVistorHelper(TypeableTerminal tt, String visitorType){
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(visitorType);
        TypeSymbol type = new TypeSymbol(resolver.fullName, false, false);
        type.setTypeDclNode(resolver);
        currentTypes.peek().add(tt);
    }

    @Override
    public void visit(NullSymbol nullSymbol) {
        simpleVistorHelper(nullSymbol, JoosNonTerminal.NULL);
    }

    @Override
    public void visit(IntegerLiteralSymbol intSymbol) {
        simpleVistorHelper(intSymbol, JoosNonTerminal.INTEGER);

    }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol) {
        simpleVistorHelper(characterSymbol, JoosNonTerminal.CHAR);
    }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol){
        simpleVistorHelper(boolSymbol, JoosNonTerminal.BOOLEAN);
    }

    @Override
    public void visit(ThisSymbol thisSymbol) {
        simpleVistorHelper(thisSymbol, enclosingClassName);
    }

    @Override
    public void visit(SuperSymbol superSymbol) throws CompilerException {
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(enclosingClassName);
        simpleVistorHelper(superSymbol, resolver.getSuperName());
    }

    @Override
    public void visit(ReturnExprSymbol returnSymbol) throws IllegalCastAssignmentException, UndeclaredException {
        /*TODO when everything else works, uncomment this. TypeSymbol currentType;
        if(!returnSymbol.children.isEmpty()) currentType = currentTypes.peek().peek().getType();
        else currentType = TypeSymbol.voidType;
        returnSymbol.setType(currentType);
        if(currentMC.type.getTypeDclNode().getCastablility(currentType.getTypeDclNode()) != Castable.DOWN_CAST){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            throw new IllegalCastAssignmentException(enclosingClassName, where, currentType.value, currentMC.type.value);
        }*/
    }
}
