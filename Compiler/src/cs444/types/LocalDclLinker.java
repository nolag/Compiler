package cs444.types;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.codegen.IPlatform;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.ByteLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodInvokeSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.NameSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.ShortLiteralSymbol;
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
import cs444.parser.symbols.ast.expressions.BinOpExpr;
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
import cs444.types.exceptions.ExplicitThisInStaticException;
import cs444.types.exceptions.ForwardReferenceException;
import cs444.types.exceptions.IllegalCastAssignmentException;
import cs444.types.exceptions.IllegalInstanceOfException;
import cs444.types.exceptions.UndeclaredException;

public class LocalDclLinker extends EmptyVisitor {
    private LocalScope currentScope;
    private final ContextInfo context;
    private final Stack<Deque<Typeable>> currentTypes = new Stack<Deque<Typeable>>();
    private final Stack<Boolean> useCurrentScopeForLookup = new Stack<Boolean>();

    private long offset = 0;

    private boolean methodArgs = false;
    private long argOffset = 0;

    boolean fromSuper = false;
    boolean superSaver = false;

    private final IPlatform<?> platform;

    public LocalDclLinker(final String enclosingClassName, final IPlatform<?> platform){
        this.context = new ContextInfo(enclosingClassName);

        currentTypes.add(new ArrayDeque<Typeable>());
        useCurrentScopeForLookup.add(false);
        this.platform = platform;
    }

    public LocalDclLinker(final String enclosingClassName, final boolean isStatic, final IPlatform<?> platform){
        this(enclosingClassName, platform);
        pushNewScope(isStatic);
    }

    // creates root scope which will contain parameters declarations
    @Override
    public void open(final MethodOrConstructorSymbol methodSymbol){
        methodArgs = true;
        //Two from return value location and stack a third for this if it is not static
        final int where = methodSymbol.isStatic() ? 2 : 3;
        argOffset = platform.getSizeHelper().getDefaultStackSize() * where;
        pushNewScope(methodSymbol.isStatic());
        context.setCurrentMember(methodSymbol);
    }

    @Override
    public void middle(final MethodOrConstructorSymbol methodOrConstructorSymbol) throws CompilerException {
        methodArgs = false;
        //Two from return value location and stack
        methodOrConstructorSymbol.setStackSize(argOffset - platform.getSizeHelper().getDefaultStackSize() * 2);
        for(final DclSymbol param : methodOrConstructorSymbol.params){
            final long stack = param.getType().getTypeDclNode().getStackSize(platform);
            argOffset -= stack;
            param.setOffset(argOffset);
        }
    }

    @Override
    public void close(final MethodOrConstructorSymbol methodSymbol){
        popCurrentScope();
        context.setCurrentMember(null);
    }

    @Override
    public void open(final DclSymbol dclSymbol) throws DuplicateDeclarationException{
        if(!dclSymbol.isLocal){ // field?
            context.setCurrentMember(dclSymbol);
            currentScope.initializing(dclSymbol.dclName);
            currentScope.add(dclSymbol.dclName, dclSymbol);
            pushNewScope(dclSymbol.isStatic());
        }else{
            currentScope.initializing(dclSymbol.dclName);
        }
    }

    @Override
    public void close(final DclSymbol dclSymbol) throws DuplicateDeclarationException, UndeclaredException, IllegalCastAssignmentException {
        final TypeSymbol initType = currentTypes.peek().removeLast().getType();

        assertIsAssignable(initType, dclSymbol.getType(), false, false);

        if (dclSymbol.isLocal){
            // in close because we cannot used this variable inside its initializer
            final String varName = dclSymbol.dclName;
            if (currentScope.isDeclared(varName)) throw new DuplicateDeclarationException(varName, context.enclosingClassName);
            currentScope.add(varName, dclSymbol);
            if(methodArgs){
                argOffset += dclSymbol.getType().getTypeDclNode().getStackSize(platform);
            }else{
                offset -= dclSymbol.getType().getTypeDclNode().getStackSize(platform);
                dclSymbol.setOffset(offset);
            }
        }else{ // field?
            popCurrentScope();
            context.setCurrentMember(null);
        }
    }

    @Override
    public void open(final NonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            pushNewScope(currentScope.isStatic);
            currentTypes.add(new ArrayDeque<Typeable>());
        }
    }

    @Override
    public void close(final NonTerminal aNonTerminal){
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)){
            aNonTerminal.setStackSize(currentScope.offset - offset);
            popCurrentScope();
            currentTypes.pop();
        }
    }

    @Override
    public void prepare(final MethodInvokeSymbol invoke) {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentScopeForLookup.push(false);

    }

    @Override
    public void open(final MethodInvokeSymbol invoke) throws CompilerException {
        superSaver = fromSuper;
        fromSuper = false;

        final Deque<Typeable> currentSymbols = currentTypes.pop();

        final APkgClassResolver resolver = currentSymbols.isEmpty() ?
                PkgClassInfo.instance.getSymbol(context.enclosingClassName) : currentSymbols.peekLast().getType().getTypeDclNode();

        final boolean isStatic = invoke.lookupFirst == null ? currentScope.isStatic : false;
        if(invoke.lookupFirst != null){
            DclSymbol dcl = null;
            dcl = currentScope.find(invoke.lookupFirst);
            if(dcl == null){
                final List<DclSymbol> dcls = resolver.findDcl(invoke.lookupFirst, isStatic, currentSymbols.isEmpty(), fromSuper);
                dcl = dcls.get(0);
                if (dcl.dclInResolver == resolver && context.insideField()){
                    throw new ForwardReferenceException(context.getMemberName(), dcl.dclName, context.enclosingClassName);
                }
                currentSymbols.addAll(dcls);
            }else{
                currentSymbols.add(dcl);
            }
            fromSuper = false;
        }

        final LookupLink lookup = new LookupLink(new LinkedList<Typeable>(currentSymbols));
        invoke.setLookup(lookup);
        useCurrentScopeForLookup.pop();
        useCurrentScopeForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(final MethodInvokeSymbol invoke) throws CompilerException {
        fromSuper = superSaver;
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
        final Deque<Typeable>currentSymbols = currentTypes.pop();

        LookupLink lookup = invoke.getLookup();

        boolean isStatic = currentScope.isStatic;
        if (isStatic && lookup.lastDcl == null) lookup = new LookupLink(Arrays.asList(
                new Typeable [] {resolver.findDcl(context.enclosingClassName, true, true, false).get(0)}));

        if(lookup.lastDcl != null){
            final TypeSymbol type = lookup.getType();
            resolver = type.getTypeDclNode();
            isStatic = type.isClass;
            if(!(lookup.lastDcl instanceof SuperSymbol))fromSuper = false;
        }

        final List<String> params = new LinkedList<String>();

        for(final Typeable mod : currentSymbols){
            final TypeSymbol type = mod.getType();
            //If it's a class this would be like the argument is String.  There is no local String.
            if(type.isClass)
                throw new UndeclaredException("Class arguments", context.getMemberName());

            final String name = type.getTypeDclNode().fullName;
            params.add(name);
        }

        final APkgClassResolver myResolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
        final AMethodSymbol method = resolver.findMethod(invoke.methodName, isStatic, params, myResolver, fromSuper);
        invoke.setCallSymbol(method);
        invoke.setLookup(invoke.getLookup().addWith(method));
        currentTypes.peek().add(method);
        fromSuper = false;
    }

    private void pushNewScope(final boolean isStatic) {
        currentScope = new LocalScope(currentScope, isStatic, offset);
    }

    private void popCurrentScope() {
        offset = currentScope.offset;
        currentScope = currentScope.parent;
    }

    @Override
    public void visit(final NameSymbol nameSymbol) throws CompilerException{
        final String [] lookupNames = nameSymbol.value.split("\\.");

        DclSymbol dclNode = null;

        if(!useCurrentScopeForLookup.peek()) dclNode = currentScope.find(lookupNames[0]);
        LookupLink link;

        if(dclNode != null){
            List<Typeable> dclList;
            if(lookupNames.length == 1){
                dclList = Arrays.asList(new Typeable [] {dclNode});

            }else{
                final APkgClassResolver resolver = dclNode.type.getTypeDclNode();
                final APkgClassResolver who = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
                final List<DclSymbol> restList =
                        resolver.findDcl(nameSymbol.value.substring(lookupNames[0].length() + 1), false, who, false, fromSuper);

                dclList = new LinkedList<Typeable>(restList);
                dclList.add(0, dclNode);

            }
            link = new LookupLink(dclList);
        }else{
            APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
            boolean isStatic = currentScope.isStatic;
            final Typeable currentSymbol = useCurrentScopeForLookup.peek() ? currentTypes.peek().getLast() : null;
            boolean allowClass = true;

            if(currentSymbol != null){
                resolver = currentSymbol.getType().getTypeDclNode();
                isStatic = currentSymbol.getType().isClass;
                allowClass = false;
            }

            final List<Typeable> modOp = new LinkedList<Typeable>(resolver.findDcl(nameSymbol.value, isStatic, allowClass, fromSuper));
            link = new LookupLink(modOp);
        }

        nameSymbol.setDclNode(link);
        currentTypes.peek().add(nameSymbol);
    }

    @Override
    public void prepare(final FieldAccessSymbol fieldAccessSymbol) {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentScopeForLookup.push(false);
    }

    @Override
    public void open(final FieldAccessSymbol access){
        useCurrentScopeForLookup.pop();
        useCurrentScopeForLookup.push(true);
    }

    @Override
    public void close(final FieldAccessSymbol access){
        final Deque<Typeable> typeableDeque = currentTypes.pop();
        useCurrentScopeForLookup.pop();
        currentTypes.peek().add(typeableDeque.getLast());
        fromSuper = false;
    }

    private void simpleVistorHelper(final TypeableTerminal tt, final String visitorType) throws UndeclaredException{
        final APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(visitorType);
        if (resolver == null) {
            throw new UndeclaredException(visitorType, context.getMemberName());
        }
        final TypeSymbol type = new TypeSymbol(resolver.fullName, false, false);
        type.setTypeDclNode(resolver);
        tt.setType(type);
        currentTypes.peek().add(tt);
    }

    @Override
    public void visit(final NullSymbol nullSymbol) throws UndeclaredException {
        simpleVistorHelper(nullSymbol, JoosNonTerminal.NULL);
    }

    @Override
    public void visit(final IntegerLiteralSymbol intSymbol) throws UndeclaredException {
        simpleVistorHelper(intSymbol, JoosNonTerminal.INTEGER);

    }

    @Override
    public void visit(final ShortLiteralSymbol shortLiteral) throws CompilerException {
        simpleVistorHelper(shortLiteral, JoosNonTerminal.SHORT);

    }

    @Override
    public void visit(final ByteLiteralSymbol byteLiteral) throws CompilerException {
        simpleVistorHelper(byteLiteral, JoosNonTerminal.BYTE);
    }

    @Override
    public void visit(final CharacterLiteralSymbol characterSymbol) throws UndeclaredException {
        simpleVistorHelper(characterSymbol, JoosNonTerminal.CHAR);
    }

    @Override
    public void visit(final BooleanLiteralSymbol boolSymbol) throws UndeclaredException{
        simpleVistorHelper(boolSymbol, JoosNonTerminal.BOOLEAN);
    }

    @Override
    public void visit(final StringLiteralSymbol stringSymbol) throws UndeclaredException{
        simpleVistorHelper(stringSymbol, JoosNonTerminal.STRING);
    }

    @Override
    public void visit(final EmptyStatementSymbol emptySymbol) throws CompilerException{
        simpleVistorHelper(emptySymbol, JoosNonTerminal.VOID);
    }

    @Override
    public void visit(final ThisSymbol thisSymbol) throws UndeclaredException, ExplicitThisInStaticException {
        if (currentScope.isStatic) {
            throw new ExplicitThisInStaticException(context.enclosingClassName, context.getCurrentMember().dclName);
        }

        simpleVistorHelper(thisSymbol, context.enclosingClassName);
    }

    @Override
    public void visit(final SuperSymbol superSymbol) throws CompilerException {
        if (currentScope.isStatic) {
            throw new ExplicitThisInStaticException(context.enclosingClassName, context.getCurrentMember().dclName);
        }
        simpleVistorHelper(superSymbol, context.enclosingClassName);
        fromSuper = true;
    }

    public void bothBooleanHelper(final BinOpExpr op) throws UndeclaredException, BadOperandsTypeException{
        final TypeSymbol second = currentTypes.peek().removeLast().getType();
        final TypeSymbol first = currentTypes.peek().removeLast().getType();

        assertIsBoolean(first);
        assertIsBoolean(second);

        final TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
        currentTypes.peek().add(boolType);
    }

    @Override
    public void visit(final AndExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(final OrExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(final EAndExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(final EOrExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(final NotOpExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        assertIsBoolean(currentTypes.peek().getLast().getType());
        final TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
    }

    @Override
    public void open(final IfExprSymbol expr){
        currentTypes.add(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(final IfExprSymbol expr) throws BadOperandsTypeException, UndeclaredException{
        assertIsBoolean(currentTypes.peek().peek().getType());
        currentTypes.pop();
    }

    @Override
    public void open(final WhileExprSymbol expr){
        currentTypes.add(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(final WhileExprSymbol expr) throws UndeclaredException, BadOperandsTypeException{
        assertIsBoolean(currentTypes.peek().peek().getType());
        currentTypes.pop();
    }

    @Override
    public void open(final ForExprSymbol expr){
        pushNewScope(currentScope.isStatic);
        currentTypes.add(new ArrayDeque<Typeable>());
    }

    @Override
    public void afterClause(final ForExprSymbol forExprSymbol){
        currentTypes.peek().clear();
    }

    @Override
    public void afterCondition(final ForExprSymbol forExprSymbol)throws CompilerException {
        assertIsBoolean(currentTypes.peek().getLast().getType());
    }

    @Override
    public void close(final ForExprSymbol expr) throws UndeclaredException, IllegalCastAssignmentException{
        currentTypes.pop();
        expr.setStackSize(currentScope.offset - offset);
        popCurrentScope();
    }

    @Override
    public void visit(final NegOpExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
        final TypeSymbol was = currentTypes.peek().getLast().getType();
        final APkgClassResolver intType = PkgClassInfo.instance.getSymbol(JoosNonTerminal.INTEGER);
        if(intType.getCastablility(was.getTypeDclNode()) == Castable.NOT_CASTABLE){
            final String where = context.getMemberName();
            final String name1 = was.isArray ? ArrayPkgClassResolver.getArrayName(was.value) : was.value;
            final String name2 = context.getCurrentMember().type.isArray ? ArrayPkgClassResolver.getArrayName(context.getCurrentMember().type.value) : context.getCurrentMember().type.value;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }
        op.setType(TypeSymbol.getPrimative(JoosNonTerminal.INTEGER));
    }

    @Override
    public void visit(final InstanceOfExprSymbol op) throws IllegalInstanceOfException, UndeclaredException {
        final TypeSymbol rhs = currentTypes.peek().removeLast().getType();
        final TypeSymbol lhs = currentTypes.peek().removeLast().getType();
        final String where = context.getMemberName();
        if(lhs.isClass)
            throw new IllegalInstanceOfException(context.enclosingClassName, where, "Classes:", context.getCurrentMember().type.value);

        if(!rhs.isClass)
            throw new IllegalInstanceOfException(context.enclosingClassName, where, "non class:", context.getCurrentMember().type.value);

        if(!lhs.isArray && JoosNonTerminal.notAllowedForInstanceOfLHS.contains(lhs.value))
            throw new IllegalInstanceOfException(context.enclosingClassName, where, lhs.value, context.getCurrentMember().type.value);

        if(!rhs.isArray && JoosNonTerminal.notAllowedForInstanceOfRHS.contains(rhs.value))
            throw new IllegalInstanceOfException(context.enclosingClassName, where, rhs.value, context.getCurrentMember().type.value);
        final TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
        currentTypes.peek().add(boolType);
    }

    private void castOrAssign(final boolean secondIsClass, final boolean allowDownCast) throws IllegalCastAssignmentException, UndeclaredException {
        final TypeSymbol isType = currentTypes.peek().removeLast().getType();
        final TypeSymbol toType = currentTypes.peek().getLast().getType();

        assertIsAssignable(isType, toType, secondIsClass, allowDownCast);
    }

    private void assertIsAssignable(final TypeSymbol type, final TypeSymbol toType, final boolean secondIsClass, final boolean allowDownCast)
            throws UndeclaredException, IllegalCastAssignmentException {

        final Castable castType = toType.getTypeDclNode().getCastablility(type.getTypeDclNode());
        if(castType == Castable.NOT_CASTABLE  || toType.isClass != secondIsClass || (castType == Castable.DOWN_CAST && !allowDownCast)
                || type.value.equals(JoosNonTerminal.VOID) || toType.value.equals(JoosNonTerminal.VOID)
                || (!type.value.equals(toType.value) && type.isArray && JoosNonTerminal.primativeNumbers.contains(toType.value))){
            final String where = context.getMemberName();
            final String name1 = type.getTypeDclNode().fullName;
            final String name2 = toType.getTypeDclNode().fullName;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }
    }

    @Override
    public void visit(final CastExpressionSymbol symbol) throws CompilerException {
        castOrAssign(true, true);
        //cast is to a class, make sure it's not a class after the cast
        final Deque<Typeable> currentDeque = currentTypes.peek();
        final TypeSymbol type = currentDeque.removeLast().getType();
        final TypeSymbol finalType = type.getNonClassVersion();
        currentDeque.add(finalType);
    }

    @Override
    public void close(final ReturnExprSymbol returnSymbol) throws IllegalCastAssignmentException, UndeclaredException {
        TypeSymbol currentType;
        if(!returnSymbol.children.isEmpty()) currentType = currentTypes.peek().getLast().getType();
        else currentType = TypeSymbol.getPrimative(JoosNonTerminal.VOID);
        returnSymbol.setType(currentType);
        if(context.getCurrentMember().type.getTypeDclNode().getCastablility(currentType.getTypeDclNode()) != Castable.UP_CAST  || currentType.isClass
                || (context.getCurrentMember().type.value == JoosNonTerminal.VOID && !returnSymbol.children.isEmpty())){
            final String where = context.getMemberName();

            final String name1 = currentType.isArray ? ArrayPkgClassResolver.getArrayName(currentType.value) : currentType.value;
            final String name2 = context.getCurrentMember().type.isArray ? ArrayPkgClassResolver.getArrayName(context.getCurrentMember().type.value) : context.getCurrentMember().type.value;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }
    }

    @Override
    public void visit(final TypeSymbol type){
        currentTypes.peek().add(type);
    }

    @Override
    public void visit(final AssignmentExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException, UnsupportedException {
        final Typeable leftHS = currentTypes.peek().removeLast();
        if(leftHS.getType().isFinal) throw new UnsupportedException("left hand side of assignment cannot be final.");

        final TypeSymbol rightHSType = currentTypes.peek().removeLast().getType();
        assertIsAssignable(rightHSType, leftHS.getType(), false, false);

        op.setType(leftHS.getType());
        currentTypes.peek().add(leftHS);
    }

    private void eqNeHelper(final BinOpExpr op) throws IllegalCastAssignmentException, UndeclaredException, BadOperandsTypeException{
        final TypeSymbol isType = currentTypes.peek().removeLast().getType();
        final TypeSymbol toType = currentTypes.peek().getLast().getType();

        final Castable castType = toType.getTypeDclNode().getCastablility(isType.getTypeDclNode());
        if(castType == Castable.NOT_CASTABLE  || toType.isClass
                || isType.value.equals(JoosNonTerminal.VOID) || toType.value.equals(JoosNonTerminal.VOID)
                || (!isType.value.equals(toType.value) && isType.isArray && JoosNonTerminal.primativeNumbers.contains(toType.value))
                || (JoosNonTerminal.primativeNumbers.contains(isType.value) && !JoosNonTerminal.primativeNumbers.contains(toType.value))
                || (!JoosNonTerminal.primativeNumbers.contains(isType.value) && JoosNonTerminal.primativeNumbers.contains(toType.value))){
            final String where = context.getMemberName();
            final String name1 = isType.getTypeDclNode().fullName;
            final String name2 = toType.getTypeDclNode().fullName;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }

        currentTypes.peek().removeLast();
        final TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
        currentTypes.peek().add(boolType);
    }

    @Override
    public void visit(final EqExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException, BadOperandsTypeException {
        eqNeHelper(op);
    }

    @Override
    public void visit(final NeExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException, BadOperandsTypeException  {
        eqNeHelper(op);
    }

    @Override
    public void visit(final LtExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
        bothIntHelper(JoosNonTerminal.BOOLEAN, op);
    }

    @Override
    public void visit(final LeExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
        bothIntHelper(JoosNonTerminal.BOOLEAN, op);
    }

    @Override
    public void visit(final AddExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        final TypeSymbol second = currentTypes.peek().removeLast().getType();
        final TypeSymbol first = currentTypes.peek().removeLast().getType();

        if ((isCastable(first, JoosNonTerminal.STRING, false) && !second.value.equals(JoosNonTerminal.VOID))
                || (isCastable(second, JoosNonTerminal.STRING, false) && !first.value.equals(JoosNonTerminal.VOID))){
            final TypeSymbol strType = TypeSymbol.getPrimative(JoosNonTerminal.STRING);
            op.setType(strType);
            currentTypes.peek().add(strType);
        }else{
            isNumeric(first, true);
            isNumeric(second, true);

            final TypeSymbol intType = TypeSymbol.getPrimative(JoosNonTerminal.INTEGER);
            op.setType(intType);
            currentTypes.peek().add(intType);
        }
    }

    @Override
    public void visit(final SubtractExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void visit(final MultiplyExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void visit(final DivideExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void visit(final RemainderExprSymbol op) throws UndeclaredException, BadOperandsTypeException  {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void open(final CreationExpression create){
        useCurrentScopeForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(final CreationExpression create) throws CompilerException{
        useCurrentScopeForLookup.pop();
        final Deque<Typeable> currentSymbols = currentTypes.pop();
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
        final TypeSymbol typeSymbol = create.getType();
        resolver = resolver.getClass(typeSymbol.value, true);
        if(typeSymbol.isArray) resolver = resolver.getArrayVersion();

        if(resolver.isAbstract()){
            throw new CompilerException(context.enclosingClassName,
                    context.getMemberName(),
                    "cannot instantiate abstract type " + typeSymbol.value);
        }

        final List<String> params = new LinkedList<String>();

        currentSymbols.pop();

        for(final Typeable mod : currentSymbols){
            final TypeSymbol type = mod.getType();
            //If it's a class this would be like the argument is String.  There is no local String.
            if(type.isClass)
                throw new UndeclaredException("Class arguments", context.getMemberName());

            final String name = type.getTypeDclNode().fullName;
            params.add(name);
        }

        if(!resolver.isBuilt) resolver.build();
        resolver.getConstructor(params, PkgClassInfo.instance.getSymbol(context.enclosingClassName));
        create.setType(typeSymbol);
        currentTypes.peek().add(create);
    }

    @Override
    public void visit(final ArrayAccessExprSymbol array) throws CompilerException{
        final TypeSymbol value = currentTypes.peek().removeLast().getType();
        final TypeSymbol in = currentTypes.peek().removeLast().getType();

        if(in.isClass)
            throw new UnsupportedException("Array access for classes in " + context.getCurrentMember().dclName + " " + context.enclosingClassName);

        if(!isNumeric(value, false))
            throw new UnsupportedException("Array access with non numeric " + value.getType().value + " " + context.getCurrentMember().dclName + " " + context.enclosingClassName);

        final TypeSymbol retType = new TypeSymbol(in.value, false, false);
        retType.setTypeDclNode(in.getTypeDclNode().accessor());
        array.setType(retType);
        currentTypes.peek().add(retType);
    }

    private void bothIntHelper(final String returnType, final BinOpExpr op) throws BadOperandsTypeException, UndeclaredException{
        final TypeSymbol second = currentTypes.peek().removeLast().getType();
        currentTypes.peek().getLast();
        final TypeSymbol first = currentTypes.peek().removeLast().getType();
        isNumeric(first, true);
        isNumeric(second, true);

        final TypeSymbol type = TypeSymbol.getPrimative(returnType);
        op.setType(type);
        currentTypes.peek().add(type);
    }

    private boolean isNumeric(final TypeSymbol type, final boolean die)
            throws UndeclaredException, BadOperandsTypeException {

        return isCastable(type, JoosNonTerminal.INTEGER, die);
    }

    private void assertIsBoolean(final TypeSymbol type) throws UndeclaredException,
    BadOperandsTypeException {

        if(type.isArray || type.isClass){
            final String where = context.getMemberName();
            throw new BadOperandsTypeException(context.enclosingClassName, where, ArrayPkgClassResolver.getArrayName(type.value), context.getCurrentMember().type.value);
        }

        if(!type.value.equals(JoosNonTerminal.BOOLEAN)){
            final String where = context.getMemberName();
            throw new BadOperandsTypeException(context.enclosingClassName, where, type.value, context.getCurrentMember().type.value);
        }
    }

    private boolean isCastable(final TypeSymbol type, final String to, final boolean die)
            throws UndeclaredException, BadOperandsTypeException {

        if(type.isArray || type.isClass){
            final String where = context.getMemberName();
            if (die) throw new BadOperandsTypeException(context.enclosingClassName, where, ArrayPkgClassResolver.getArrayName(type.value), context.getCurrentMember().type.value);
            return false;
        }

        final APkgClassResolver typeResolver = PkgClassInfo.instance.getSymbol(to);
        if (typeResolver == null) {
            throw new UndeclaredException(to, context.getMemberName());
        }
        if(typeResolver.getCastablility(type.getTypeDclNode()) == Castable.NOT_CASTABLE){
            final String where = context.getMemberName();
            if (die) throw new BadOperandsTypeException(context.enclosingClassName, where, type.value, context.getCurrentMember().type.value);
            return false;
        }
        return true;
    }
}
