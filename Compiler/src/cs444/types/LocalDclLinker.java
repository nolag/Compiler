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
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.ast.SuperSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.TypeableTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;
import cs444.parser.symbols.ast.expressions.LeExprSymbol;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;
import cs444.parser.symbols.ast.expressions.NotOpExprSymbol;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.APkgClassResolver.Castable;
import cs444.types.exceptions.BadOperandsTypeException;
import cs444.types.exceptions.DuplicateDeclarationException;
import cs444.types.exceptions.IllegalCastAssignmentException;
import cs444.types.exceptions.IllegalInstanceOfException;
import cs444.types.exceptions.ImplicitStaticConversionException;
import cs444.types.exceptions.UndeclaredException;

public class LocalDclLinker extends EmptyVisitor {
    //TODO remove this when linking is done for everything
    public static boolean checkTypes = false;

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
            currentTypes.add(new ArrayDeque<Typeable>());
        }
    }

    @Override
    public void close(NonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            popCurrentScope();
            currentTypes.pop();
        }
    }

    @Override
    public void open(MethodInvokeSymbol invoke) throws UndeclaredException, ImplicitStaticConversionException {

        Deque<Typeable> currentSymbols = currentTypes.pop();

        APkgClassResolver resolver = currentSymbols.isEmpty() ?
                PkgClassInfo.instance.getSymbol(enclosingClassName) : currentSymbols.peekLast().getType().getTypeDclNode();

        boolean isStatic = invoke.children.isEmpty() ? currentScope.isStatic : false;
        if(invoke.lookupFirst != null){
            DclSymbol dcl = null;
            if(invoke.children.isEmpty()) dcl = currentScope.find(invoke.lookupFirst);
            if(dcl == null)dcl = resolver.getDcl(invoke.lookupFirst, isStatic, resolver, isStatic);
            currentSymbols.add(dcl);
            resolver = dcl.type.getTypeDclNode();
            isStatic = dcl.type.isClass;
        }

        LookupLink lookup = new LookupLink(new LinkedList<Typeable>(currentSymbols));
        invoke.setLookup(lookup);
        useCurrentForLookup.pop();
        useCurrentForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(MethodInvokeSymbol invoke) throws UndeclaredException {
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
            TypeSymbol type = mod.getType();
            //If it's a class this would be like the argument is String.  There is no local String.
            if(type.isClass)
                throw new UndeclaredException("Class arguments", APkgClassResolver.generateUniqueName(currentMC, currentMC.dclName));

            String name = type.getTypeDclNode().fullName;
            //if(type.isArray) name = ArrayPkgClassResolver.getArrayName(name);
            params.add(name);
        }

if(checkTypes){
        AMethodSymbol method = resolver.findMethod(invoke.methodName, isStatic, params);
        invoke.setLookup(invoke.getLookup().addWith(method));
        currentTypes.peek().add(method);
}
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
                List<DclSymbol> restList = resolver.findDcl(nameSymbol.value.substring(lookupNames[0].length() + 1), false, false);
                dclList = new LinkedList<Typeable>(restList);
                dclList.add(0, dclNode);

            }
            link = new LookupLink(dclList);
        }else{
            APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(enclosingClassName);
            boolean isStatic = currentScope.isStatic;
            Typeable currentSymbol = useCurrentForLookup.peek() ? currentTypes.peek().getLast() : null;
            boolean allowClass = true;

            if(currentSymbol != null){
                resolver = currentSymbol.getType().getTypeDclNode();
                isStatic = currentSymbol.getType().isClass;
                allowClass = false;
            }

            List<Typeable> modOp = new LinkedList<Typeable>(resolver.findDcl(nameSymbol.value, isStatic, allowClass));
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
    public void prepare(FieldAccessSymbol fieldAccessSymbol) {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentForLookup.push(false);
    }

    @Override
    public void open(FieldAccessSymbol access){
        useCurrentForLookup.pop();
        useCurrentForLookup.push(true);
    }

    @Override
    public void close(FieldAccessSymbol access){
        Deque<Typeable> typeableDeque = currentTypes.pop();
        useCurrentForLookup.pop();
        currentTypes.peek().add(typeableDeque.getLast());
    }

    private void simpleVistorHelper(TypeableTerminal tt, String visitorType) throws UndeclaredException{
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(visitorType);
        if (resolver == null) {
            throw new UndeclaredException(visitorType, PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName));
        }
        TypeSymbol type = new TypeSymbol(resolver.fullName, false, false);
        type.setTypeDclNode(resolver);
        tt.setType(type);
        currentTypes.peek().add(tt);
    }

    @Override
    public void visit(NullSymbol nullSymbol) throws UndeclaredException {
        simpleVistorHelper(nullSymbol, JoosNonTerminal.NULL);
    }

    @Override
    public void visit(IntegerLiteralSymbol intSymbol) throws UndeclaredException {
        simpleVistorHelper(intSymbol, JoosNonTerminal.INTEGER);

    }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol) throws UndeclaredException {
        simpleVistorHelper(characterSymbol, JoosNonTerminal.CHAR);
    }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol) throws UndeclaredException{
        simpleVistorHelper(boolSymbol, JoosNonTerminal.BOOLEAN);
    }

    @Override
    public void visit(StringLiteralSymbol stringSymbol) throws UndeclaredException{
        simpleVistorHelper(stringSymbol, JoosNonTerminal.STRING);
    }

    @Override
    public void visit(EmptyStatementSymbol emptySymbol) throws CompilerException{
        simpleVistorHelper(emptySymbol, JoosNonTerminal.VOID);
    }

    @Override
    public void visit(ThisSymbol thisSymbol) throws UndeclaredException {
        simpleVistorHelper(thisSymbol, enclosingClassName);
    }

    @Override
    public void visit(SuperSymbol superSymbol) throws CompilerException {
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(enclosingClassName);
        simpleVistorHelper(superSymbol, resolver.getSuperName());
    }

    public void bothBooleanHelper() throws IllegalCastAssignmentException, UndeclaredException{
        TypeSymbol second = currentTypes.peek().removeLast().getType();
        TypeSymbol first = currentTypes.peek().removeLast().getType();
        APkgClassResolver booleanType = PkgClassInfo.instance.getSymbol(JoosNonTerminal.BOOLEAN);

        if(first.isArray || second.isArray){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            throw new IllegalCastAssignmentException(enclosingClassName, where, ArrayPkgClassResolver.getArrayName(first.value), currentMC.type.value);
        }

        if(first.isClass || second.isClass){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            throw new IllegalCastAssignmentException(enclosingClassName, where, "Class types", currentMC.type.value);
        }

        if(booleanType.getCastablility(first.getTypeDclNode()) == Castable.NOT_CASTABLE){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            throw new IllegalCastAssignmentException(enclosingClassName, where, first.value, currentMC.type.value);
        }

        if(booleanType.getCastablility(second.getTypeDclNode()) == Castable.NOT_CASTABLE){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            throw new IllegalCastAssignmentException(enclosingClassName, where, first.value, currentMC.type.value);
        }

        currentTypes.peek().add(TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN));
    }

    @Override
    public void visit(AndExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        bothBooleanHelper();
}
    }

    @Override
    public void visit(OrExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        bothBooleanHelper();
}
    }

    @Override
    public void visit(EAndExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        bothBooleanHelper();
}
    }

    @Override
    public void visit(EOrExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        bothBooleanHelper();
}
    }

    @Override
    public void visit(NotOpExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        assertIsBoolean();
}
    }

    private void assertIsBoolean() throws UndeclaredException,
            IllegalCastAssignmentException {
        TypeSymbol was = currentTypes.peek().peek().getType();
        APkgClassResolver booleanType = PkgClassInfo.instance.getSymbol(JoosNonTerminal.BOOLEAN);
        if(booleanType.getCastablility(was.getTypeDclNode()) == Castable.NOT_CASTABLE){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            String name1 = was.isArray ? ArrayPkgClassResolver.getArrayName(was.value) : was.value;
            throw new IllegalCastAssignmentException(enclosingClassName, where, name1, "boolean");
        }
    }

    @Override
    public void open(IfExprSymbol expr){
if(checkTypes){
        currentTypes.add(new ArrayDeque<Typeable>());
}
    }

    @Override
    public void close(IfExprSymbol expr) throws UndeclaredException, IllegalCastAssignmentException{
if(checkTypes){
        assertIsBoolean();
        currentTypes.pop();
}
    }

    @Override
    public void open(WhileExprSymbol expr){
if(checkTypes){
        currentTypes.add(new ArrayDeque<Typeable>());
}
    }

    @Override
    public void close(WhileExprSymbol expr) throws UndeclaredException, IllegalCastAssignmentException{
if(checkTypes){
        assertIsBoolean();
        currentTypes.pop();
}
    }

    @Override
    public void open(ForExprSymbol expr){
if(checkTypes){
        pushNewScope(currentScope.isStatic);
        currentTypes.add(new ArrayDeque<Typeable>());
}
    }

    @Override
    public void afterClause(ForExprSymbol forExprSymbol)
            throws CompilerException {
if(checkTypes){
        currentTypes.peek().clear();
}
    }

    @Override
    public void afterCondition(ForExprSymbol forExprSymbol)
            throws CompilerException {
if(checkTypes){
        assertIsBoolean();
}
    }

    @Override
    public void close(ForExprSymbol expr) throws UndeclaredException, IllegalCastAssignmentException{
if(checkTypes){
        currentTypes.pop();
        popCurrentScope();
}
    }

    @Override
    public void visit(NegOpExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        TypeSymbol was = currentTypes.peek().peek().getType();
        APkgClassResolver intType = PkgClassInfo.instance.getSymbol(JoosNonTerminal.INTEGER);
        if(intType.getCastablility(was.getTypeDclNode()) == Castable.NOT_CASTABLE){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            String name1 = was.isArray ? ArrayPkgClassResolver.getArrayName(was.value) : was.value;
            String name2 = currentMC.type.isArray ? ArrayPkgClassResolver.getArrayName(currentMC.type.value) : currentMC.type.value;
            throw new IllegalCastAssignmentException(enclosingClassName, where, name1, name2);
        }
}
    }

    @Override
    public void visit(InstanceOfExprSymbol op) throws IllegalInstanceOfException, UndeclaredException {
if(checkTypes){
        TypeSymbol rhs = currentTypes.peek().removeLast().getType();
        TypeSymbol lhs = currentTypes.peek().removeLast().getType();
        String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
        if(lhs.isClass)
            throw new IllegalInstanceOfException(enclosingClassName, where, "Classes:", currentMC.type.value);

        if(!rhs.isClass)
            throw new IllegalInstanceOfException(enclosingClassName, where, "non class:", currentMC.type.value);

        if(!lhs.isArray && JoosNonTerminal.notAllowedForInstanceOfLHS.contains(lhs.value))
            throw new IllegalInstanceOfException(enclosingClassName, where, lhs.value, currentMC.type.value);

        if(!rhs.isArray && JoosNonTerminal.notAllowedForInstanceOfRHS.contains(rhs.value))
            throw new IllegalInstanceOfException(enclosingClassName, where, rhs.value, currentMC.type.value);
        currentTypes.peek().add(TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN));
}
    }

    private void castOrAssign(boolean secondIsClass, boolean allowDownCast) throws IllegalCastAssignmentException, UndeclaredException {
        TypeSymbol isType = currentTypes.peek().removeLast().getType();
        TypeSymbol toType = currentTypes.peek().getLast().getType();

        Castable castType = toType.getTypeDclNode().getCastablility(isType.getTypeDclNode());
        if(castType == Castable.NOT_CASTABLE  || toType.isClass != secondIsClass || (castType == Castable.DOWN_CAST && !allowDownCast)){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            String name1 = isType.isArray ? ArrayPkgClassResolver.getArrayName(isType.value) : isType.value;
            String name2 = toType.isArray ? ArrayPkgClassResolver.getArrayName(toType.value) : toType.value;
            throw new IllegalCastAssignmentException(enclosingClassName, where, name1, name2);
        }
    }

    @Override
    public void visit(CastExpressionSymbol symbol) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        castOrAssign(true, true);
        //cast is to a class, make sure it's not a class after the cast
        Deque<Typeable> currentDeque = currentTypes.peek();
        TypeSymbol type = currentDeque.removeLast().getType();
        currentDeque.add(type.getNonClassVersion());
}
    }

    @Override
    public void close(ReturnExprSymbol returnSymbol) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        TypeSymbol currentType;
        if(!returnSymbol.children.isEmpty()) currentType = currentTypes.peek().getLast().getType();
        else currentType = TypeSymbol.getPrimative(JoosNonTerminal.VOID);
        returnSymbol.setType(currentType);
        if(currentMC.type.getTypeDclNode().getCastablility(currentType.getTypeDclNode()) != Castable.UP_CAST  || currentType.isClass){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);

            String name1 = currentType.isArray ? ArrayPkgClassResolver.getArrayName(currentType.value) : currentType.value;
            String name2 = currentMC.type.isArray ? ArrayPkgClassResolver.getArrayName(currentMC.type.value) : currentMC.type.value;
            throw new IllegalCastAssignmentException(enclosingClassName, where, name1, name2);
        }
}
    }

    @Override
    public void visit(TypeSymbol type){
        currentTypes.peek().add(type);
    }

    @Override
    public void visit(AssignmentExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        castOrAssign(false, false);
}
    }

    private void eqNeHelper() throws IllegalCastAssignmentException, UndeclaredException{
         castOrAssign(false, true);
         currentTypes.peek().removeLast();
         currentTypes.peek().add(TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN));
    }

    @Override
    public void visit(EqExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
if(checkTypes){
        eqNeHelper();
}
    }

    @Override
    public void visit(NeExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException  {
if(checkTypes){
        eqNeHelper();
}
    }

    @Override
    public void visit(LtExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
if(checkTypes){
        bothIntHelper(JoosNonTerminal.BOOLEAN);
}
    }

    @Override
    public void visit(LeExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
if(checkTypes){
        bothIntHelper(JoosNonTerminal.BOOLEAN);
}
    }

    @Override
    public void visit(AddExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
if(checkTypes){
        if (isNumeric(currentTypes.peek().peek().getType(), false)){
            bothIntHelper(JoosNonTerminal.INTEGER);
        }else{
            bothStringHelper(JoosNonTerminal.STRING);
        }
}
    }

    @Override
    public void visit(SubtractExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
if(checkTypes){
        bothIntHelper(JoosNonTerminal.INTEGER);
}
    }

    @Override
    public void visit(MultiplyExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
if(checkTypes){
        bothIntHelper(JoosNonTerminal.INTEGER);
}
    }

    @Override
    public void visit(DivideExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
if(checkTypes){
        bothIntHelper(JoosNonTerminal.INTEGER);
}
    }

    @Override
    public void visit(RemainderExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
if(checkTypes){
        bothIntHelper(JoosNonTerminal.INTEGER);
}
    }

    @Override
    public void open(CreationExpression create){
if(checkTypes){
        useCurrentForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
}
    }

    @Override
    public void close(CreationExpression create) throws UndeclaredException{
if(checkTypes){
        useCurrentForLookup.pop();
        Deque<Typeable> currentSymbols = currentTypes.pop();
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(enclosingClassName);
        TypeSymbol typeSymbol = create.getType();
        resolver = resolver.getClass(typeSymbol.value, true);
        if(typeSymbol.isArray) resolver = resolver.getArrayVersion();

        List<String> params = new LinkedList<String>();

        currentSymbols.pop();

        for(Typeable mod : currentSymbols){
            TypeSymbol type = mod.getType();
            //If it's a class this would be like the argument is String.  There is no local String.
            if(type.isClass)
                throw new UndeclaredException("Class arguments", APkgClassResolver.generateUniqueName(currentMC, currentMC.dclName));

            String name = type.getTypeDclNode().fullName;
            //if(type.isArray) name = ArrayPkgClassResolver.getArrayName(name);
            params.add(name);
        }


        resolver.getConstructor(params);
        currentTypes.peek().add(create);
}
    }

    @Override
    public void visit(ArrayAccessExprSymbol array) throws CompilerException{
if(checkTypes){
        TypeSymbol value = currentTypes.peek().removeLast().getType();
        TypeSymbol in = currentTypes.peek().removeLast().getType();

        if(in.isClass)
            throw new UnsupportedException("Array access for calsses in " + currentMC.dclName + " " + enclosingClassName);

        APkgClassResolver valueResolver = value.getTypeDclNode();
        APkgClassResolver intResovler = TypeSymbol.getPrimative(JoosNonTerminal.INTEGER).getTypeDclNode();

        if(valueResolver.getCastablility(intResovler) != Castable.UP_CAST || value.isClass || value.isArray)
            throw new UnsupportedException("Array access with non int " + value.getType().value + " " + currentMC.dclName + " " + enclosingClassName);

        TypeSymbol retType = new TypeSymbol(value.value, false, false);
        retType.setTypeDclNode(in.getTypeDclNode().accessor());
        currentTypes.peek().add(retType);
}
    }

    private void bothIntHelper(String returnType) throws BadOperandsTypeException, UndeclaredException{
        TypeSymbol second = currentTypes.peek().removeLast().getType();
        TypeSymbol first = currentTypes.peek().removeLast().getType();
        isNumeric(first, true);
        isNumeric(second, true);

        currentTypes.peek().add(TypeSymbol.getPrimative(returnType));
    }

    private void bothStringHelper(String returnType) throws BadOperandsTypeException, UndeclaredException{
        TypeSymbol second = currentTypes.peek().removeLast().getType();
        TypeSymbol first = currentTypes.peek().removeLast().getType();
        isCastable(first, JoosNonTerminal.STRING, true);
        isCastable(second, JoosNonTerminal.STRING, true);

        currentTypes.peek().add(TypeSymbol.getPrimative(returnType));
    }

    private boolean isNumeric(TypeSymbol type, boolean die)
            throws UndeclaredException, BadOperandsTypeException {

        return isCastable(type, JoosNonTerminal.INTEGER, die);
    }

    private boolean isCastable(TypeSymbol type, String to, boolean die)
            throws UndeclaredException, BadOperandsTypeException {

        if(type.isArray || type.isClass){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            if (die) throw new BadOperandsTypeException(enclosingClassName, where, ArrayPkgClassResolver.getArrayName(type.value), currentMC.type.value);
            return false;
        }

        APkgClassResolver intType = PkgClassInfo.instance.getSymbol(to);
        if (intType == null) {
            throw new UndeclaredException(to, PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName));
        }
        if(intType.getCastablility(type.getTypeDclNode()) == Castable.NOT_CASTABLE){
            String where = PkgClassResolver.generateUniqueName(currentMC, currentMC.dclName);
            if (die) throw new BadOperandsTypeException(enclosingClassName, where, type.value, currentMC.type.value);
            return false;
        }
        return true;
    }
}
