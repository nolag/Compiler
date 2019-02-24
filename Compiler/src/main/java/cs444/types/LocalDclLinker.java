package cs444.types;

import cs444.CompilerException;
import cs444.ast.EmptyVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.NonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.expressions.*;
import cs444.parser.symbols.exceptions.UnsupportedException;
import cs444.types.APkgClassResolver.Castable;
import cs444.types.exceptions.*;

import java.util.*;

public class LocalDclLinker extends EmptyVisitor {
    private final ContextInfo context;
    private final Stack<Deque<Typeable>> currentTypes = new Stack<Deque<Typeable>>();
    private final Stack<Boolean> useCurrentScopeForLookup = new Stack<Boolean>();
    private final Map<Platform<?, ?>, Long> argOffsets;
    private final Collection<Platform<?, ?>> platforms;
    boolean fromSuper = false;
    boolean superSaver = false;
    private LocalScope currentScope;
    private boolean methodArgs = false;
    private Map<Platform<?, ?>, Long> offsets;

    public LocalDclLinker(String enclosingClassName, Collection<Platform<?, ?>> platforms) {
        context = new ContextInfo(enclosingClassName);

        currentTypes.add(new ArrayDeque<Typeable>());
        useCurrentScopeForLookup.add(false);
        this.platforms = platforms;
        argOffsets = new HashMap<>(platforms.size());
        offsets = new HashMap<>(platforms.size());

        for (Platform<?, ?> platform : platforms) {
            argOffsets.put(platform, 0L);
            offsets.put(platform, 0L);
        }
    }

    public LocalDclLinker(String enclosingClassName, boolean isStatic,
                          Collection<Platform<?, ?>> platforms) {
        this(enclosingClassName, platforms);
        pushNewScope(isStatic);
    }

    // creates root scope which will contain parameters declarations
    @Override
    public void open(MethodOrConstructorSymbol methodSymbol) {
        methodArgs = true;
        //Two from return value location and stack a third for this if it is not static
        long where = methodSymbol.isStatic() ? 2 : 3;

        for (Platform<?, ?> platform : platforms) {
            argOffsets.put(platform, platform.getSizeHelper().getDefaultStackSize() * where);
        }

        pushNewScope(methodSymbol.isStatic());
        context.setCurrentMember(methodSymbol);
    }

    @Override
    public void middle(MethodOrConstructorSymbol methodOrConstructorSymbol) throws CompilerException {
        methodArgs = false;
        for (Platform<?, ?> platform : platforms) {
            long argOffset = argOffsets.get(platform);
            //Two from return value location and stack
            methodOrConstructorSymbol.setStackSize(platform,
                    argOffset - platform.getSizeHelper().getDefaultStackSize() * 2);
            for (DclSymbol param : methodOrConstructorSymbol.params) {
                long stack = param.getType().getTypeDclNode().getRefStackSize(platform.getSizeHelper());
                argOffset -= stack;
                argOffsets.put(platform, argOffset);
                param.setOffset(argOffset, platform);
            }
        }
    }

    @Override
    public void close(MethodOrConstructorSymbol methodSymbol) {
        popCurrentScope();
        context.setCurrentMember(null);
    }

    @Override
    public void open(DclSymbol dclSymbol) throws DuplicateDeclarationException {
        if (!dclSymbol.isLocal) { // field?
            context.setCurrentMember(dclSymbol);
            currentScope.initializing(dclSymbol.dclName);
            currentScope.add(dclSymbol.dclName, dclSymbol);
            pushNewScope(dclSymbol.isStatic());
        } else {
            currentScope.initializing(dclSymbol.dclName);
        }
    }

    @Override
    public void close(DclSymbol dclSymbol) throws CompilerException {
        TypeSymbol initType = currentTypes.peek().removeLast().getType();
        TypeSymbol dclType = dclSymbol.getType();

        if (dclSymbol.children.size() > 0) {
            assertIsAssignable(initType, dclType, false, false, dclSymbol.children.get(0));
        }

        if (dclSymbol.isLocal) {
            // in close because we cannot used this variable inside its initializer
            String varName = dclSymbol.dclName;
            if (currentScope.isDeclared(varName)) {
                throw new DuplicateDeclarationException(varName, context.enclosingClassName);
            }
            currentScope.add(varName, dclSymbol);
        }

        if (dclSymbol.isLocal) {
            for (Platform<?, ?> platform : platforms) {
                SizeHelper<?, ?> sizeHelper = platform.getSizeHelper();
                if (methodArgs) {
                    long argOffset = argOffsets.get(platform);
                    argOffset += dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper);
                    argOffsets.put(platform, argOffset);
                } else {
                    long offset = offsets.get(platform);
                    offset -= dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper);
                    offsets.put(platform, offset);
                    dclSymbol.setOffset(offset, platform);
                }
            }
        } else {
            popCurrentScope();
            context.setCurrentMember(null);
        }
    }

    @Override
    public void open(NonTerminal aNonTerminal) {
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)) {
            pushNewScope(currentScope.isStatic);
            currentTypes.add(new ArrayDeque<Typeable>());
        }
    }

    @Override
    public void close(NonTerminal aNonTerminal) {
        if (aNonTerminal.getName().equals(JoosNonTerminal.BLOCK)) {

            for (Platform<?, ?> platform : platforms) {
                aNonTerminal.setStackSize(platform, currentScope.offsets.get(platform) - offsets.get(platform));
            }

            popCurrentScope();
            currentTypes.pop();
        }
    }

    @Override
    public void prepare(MethodInvokeSymbol invoke) {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentScopeForLookup.push(false);
    }

    @Override
    public void open(MethodInvokeSymbol invoke) throws CompilerException {
        superSaver = fromSuper;
        fromSuper = false;

        Deque<Typeable> currentSymbols = currentTypes.pop();

        APkgClassResolver resolver = currentSymbols.isEmpty() ?
                PkgClassInfo.instance.getSymbol(context.enclosingClassName) :
                currentSymbols.peekLast().getType().getTypeDclNode();

        boolean isStatic = invoke.lookupFirst == null && currentScope.isStatic;
        if (invoke.lookupFirst != null) {
            DclSymbol dcl = null;
            dcl = currentScope.find(invoke.lookupFirst);
            if (dcl == null) {
                List<DclSymbol> dcls = resolver.findDcl(invoke.lookupFirst, isStatic, currentSymbols.isEmpty(),
                        fromSuper);
                dcl = dcls.get(0);
                if (dcl.dclInResolver == resolver && context.insideField()) {
                    throw new ForwardReferenceException(context.getMemberName(), dcl.dclName,
                            context.enclosingClassName);
                }
                currentSymbols.addAll(dcls);
            } else {
                currentSymbols.add(dcl);
            }
            fromSuper = false;
        }

        LookupLink lookup = new LookupLink(new LinkedList<Typeable>(currentSymbols));
        invoke.setLookup(lookup);
        useCurrentScopeForLookup.pop();
        useCurrentScopeForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(MethodInvokeSymbol invoke) throws CompilerException {
        fromSuper = superSaver;
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
        Deque<Typeable> currentSymbols = currentTypes.pop();

        LookupLink lookup = invoke.getLookup();

        boolean isStatic = currentScope.isStatic;
        if (isStatic && lookup.lastDcl == null) {
            lookup = new LookupLink(Arrays.asList(
                    new Typeable[]{resolver.findDcl(context.enclosingClassName, true, true, false).get(0)}));
        }

        if (lookup.lastDcl != null) {
            TypeSymbol type = lookup.getType();
            resolver = type.getTypeDclNode();
            isStatic = type.isClass;
            if (!(lookup.lastDcl instanceof SuperSymbol)) {
                fromSuper = false;
            }
        }

        List<String> params = new LinkedList<String>();

        for (Typeable mod : currentSymbols) {
            TypeSymbol type = mod.getType();
            //If it's a class this would be like the argument is String.  There is no local String.
            if (type.isClass) {
                throw new UndeclaredException("Class arguments", context.getMemberName());
            }

            String name = type.getTypeDclNode().fullName;
            params.add(name);
        }

        APkgClassResolver myResolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
        AMethodSymbol method = resolver.findMethod(invoke.methodName, isStatic, params, myResolver, fromSuper);
        invoke.setCallSymbol(method);
        invoke.setLookup(invoke.getLookup().addWith(method));
        currentTypes.peek().add(method);
        fromSuper = false;
    }

    private void pushNewScope(boolean isStatic) {
        currentScope = new LocalScope(currentScope, isStatic, offsets);
    }

    private void popCurrentScope() {
        offsets = currentScope.offsets;
        currentScope = currentScope.parent;
    }

    @Override
    public void visit(NameSymbol nameSymbol) throws CompilerException {
        String[] lookupNames = nameSymbol.value.split("\\.");

        DclSymbol dclNode = null;

        if (!useCurrentScopeForLookup.peek()) {
            dclNode = currentScope.find(lookupNames[0]);
        }
        LookupLink link;

        if (dclNode != null) {
            List<Typeable> dclList;
            if (lookupNames.length == 1) {
                dclList = Arrays.asList(new Typeable[]{dclNode});
            } else {
                APkgClassResolver resolver = dclNode.type.getTypeDclNode();
                APkgClassResolver who = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
                List<DclSymbol> restList =
                        resolver.findDcl(nameSymbol.value.substring(lookupNames[0].length() + 1), false, who, false,
                                fromSuper);

                dclList = new LinkedList<Typeable>(restList);
                dclList.add(0, dclNode);
            }
            link = new LookupLink(dclList);
        } else {
            APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
            boolean isStatic = currentScope.isStatic;
            Typeable currentSymbol = useCurrentScopeForLookup.peek() ? currentTypes.peek().getLast() : null;
            boolean allowClass = true;

            if (currentSymbol != null) {
                resolver = currentSymbol.getType().getTypeDclNode();
                isStatic = currentSymbol.getType().isClass;
                allowClass = false;
            }

            List<Typeable> modOp = new LinkedList<Typeable>(resolver.findDcl(nameSymbol.value, isStatic,
                    allowClass, fromSuper));
            link = new LookupLink(modOp);
        }

        nameSymbol.setDclNode(link);
        currentTypes.peek().add(nameSymbol);
    }

    @Override
    public void prepare(FieldAccessSymbol fieldAccessSymbol) {
        currentTypes.push(new ArrayDeque<Typeable>());
        useCurrentScopeForLookup.push(false);
    }

    @Override
    public void open(FieldAccessSymbol access) {
        useCurrentScopeForLookup.pop();
        useCurrentScopeForLookup.push(true);
    }

    @Override
    public void close(FieldAccessSymbol access) {
        Deque<Typeable> typeableDeque = currentTypes.pop();
        useCurrentScopeForLookup.pop();
        currentTypes.peek().add(typeableDeque.getLast());
        fromSuper = false;
    }

    private void simpleVistorHelper(TypeableTerminal tt, String visitorType) throws UndeclaredException {
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(visitorType);
        if (resolver == null) {
            throw new UndeclaredException(visitorType, context.getMemberName());
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
    public void visit(LongLiteralSymbol longSymbol) throws UndeclaredException {
        simpleVistorHelper(longSymbol, JoosNonTerminal.LONG);
    }

    @Override
    public void visit(ShortLiteralSymbol shortLiteral) throws CompilerException {
        simpleVistorHelper(shortLiteral, JoosNonTerminal.SHORT);
    }

    @Override
    public void visit(ByteLiteralSymbol byteLiteral) throws CompilerException {
        simpleVistorHelper(byteLiteral, JoosNonTerminal.BYTE);
    }

    @Override
    public void visit(CharacterLiteralSymbol characterSymbol) throws UndeclaredException {
        simpleVistorHelper(characterSymbol, JoosNonTerminal.CHAR);
    }

    @Override
    public void visit(BooleanLiteralSymbol boolSymbol) throws UndeclaredException {
        simpleVistorHelper(boolSymbol, JoosNonTerminal.BOOLEAN);
    }

    @Override
    public void visit(StringLiteralSymbol stringSymbol) throws UndeclaredException {
        simpleVistorHelper(stringSymbol, JoosNonTerminal.STRING);
    }

    @Override
    public void visit(EmptyStatementSymbol emptySymbol) throws CompilerException {
        simpleVistorHelper(emptySymbol, JoosNonTerminal.VOID);
    }

    @Override
    public void visit(ThisSymbol thisSymbol) throws UndeclaredException, ExplicitThisInStaticException {
        if (currentScope.isStatic) {
            throw new ExplicitThisInStaticException(context.enclosingClassName, context.getCurrentMember().dclName);
        }

        simpleVistorHelper(thisSymbol, context.enclosingClassName);
    }

    @Override
    public void visit(SuperSymbol superSymbol) throws CompilerException {
        if (currentScope.isStatic) {
            throw new ExplicitThisInStaticException(context.enclosingClassName, context.getCurrentMember().dclName);
        }
        simpleVistorHelper(superSymbol, context.enclosingClassName);
        fromSuper = true;
    }

    public void bothBooleanHelper(BinOpExpr op) throws UndeclaredException, BadOperandsTypeException {
        TypeSymbol second = currentTypes.peek().removeLast().getType();
        TypeSymbol first = currentTypes.peek().removeLast().getType();

        assertIsBoolean(first);
        assertIsBoolean(second);

        TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
        currentTypes.peek().add(boolType);
    }

    @Override
    public void visit(AndExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(OrExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(EAndExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(EOrExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        bothBooleanHelper(op);
    }

    @Override
    public void visit(NotOpExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        assertIsBoolean(currentTypes.peek().getLast().getType());
        TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
    }

    @Override
    public void open(IfExprSymbol expr) {
        currentTypes.add(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(IfExprSymbol expr) throws BadOperandsTypeException, UndeclaredException {
        assertIsBoolean(currentTypes.peek().peek().getType());
        currentTypes.pop();
    }

    @Override
    public void open(WhileExprSymbol expr) {
        currentTypes.add(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(WhileExprSymbol expr) throws UndeclaredException, BadOperandsTypeException {
        assertIsBoolean(currentTypes.peek().peek().getType());
        currentTypes.pop();
    }

    @Override
    public void open(ForExprSymbol expr) {
        pushNewScope(currentScope.isStatic);
        currentTypes.add(new ArrayDeque<Typeable>());
    }

    @Override
    public void afterClause(ForExprSymbol forExprSymbol) {
        currentTypes.peek().clear();
    }

    @Override
    public void afterCondition(ForExprSymbol forExprSymbol) throws CompilerException {
        assertIsBoolean(currentTypes.peek().getLast().getType());
    }

    @Override
    public void close(ForExprSymbol expr) throws UndeclaredException, IllegalCastAssignmentException {
        currentTypes.pop();

        for (Platform<?, ?> platform : platforms) {
            expr.setStackSize(platform, currentScope.offsets.get(platform) - offsets.get(platform));
        }

        popCurrentScope();
    }

    @Override
    public void visit(NegOpExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException {
        TypeSymbol was = currentTypes.peek().getLast().getType();
        APkgClassResolver intType = PkgClassInfo.instance.getSymbol(JoosNonTerminal.INTEGER);
        if (intType.getCastablility(was.getTypeDclNode()) == Castable.NOT_CASTABLE) {
            String where = context.getMemberName();
            String name1 = was.isArray ? ArrayPkgClassResolver.getArrayName(was.value) : was.value;
            String name2 = context.getCurrentMember().type.isArray ?
                    ArrayPkgClassResolver.getArrayName(context.getCurrentMember().type.value) :
                    context.getCurrentMember().type.value;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }

        if (was.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)) {
            op.setType(TypeSymbol.getPrimative(JoosNonTerminal.LONG));
        } else {
            op.setType(TypeSymbol.getPrimative(JoosNonTerminal.INTEGER));
        }
    }

    @Override
    public void visit(InstanceOfExprSymbol op) throws IllegalInstanceOfException, UndeclaredException {
        TypeSymbol rhs = currentTypes.peek().removeLast().getType();
        TypeSymbol lhs = currentTypes.peek().removeLast().getType();
        String where = context.getMemberName();
        if (lhs.isClass) {
            throw new IllegalInstanceOfException(context.enclosingClassName, where, "Classes:",
                    context.getCurrentMember().type.value);
        }

        if (!rhs.isClass) {
            throw new IllegalInstanceOfException(context.enclosingClassName, where, "non class:",
                    context.getCurrentMember().type.value);
        }

        if (!lhs.isArray && JoosNonTerminal.notAllowedForInstanceOfLHS.contains(lhs.value)) {
            throw new IllegalInstanceOfException(context.enclosingClassName, where, lhs.value,
                    context.getCurrentMember().type.value);
        }

        if (!rhs.isArray && JoosNonTerminal.notAllowedForInstanceOfRHS.contains(rhs.value)) {
            throw new IllegalInstanceOfException(context.enclosingClassName, where, rhs.value,
                    context.getCurrentMember().type.value);
        }
        TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
        currentTypes.peek().add(boolType);
    }

    private void castOrAssign(boolean secondIsClass, boolean allowDownCast) throws IllegalCastAssignmentException,
            UndeclaredException {
        TypeSymbol isType = currentTypes.peek().removeLast().getType();
        TypeSymbol toType = currentTypes.peek().getLast().getType();

        assertIsAssignable(isType, toType, secondIsClass, allowDownCast, null);
    }

    private void assertIsAssignable(TypeSymbol type, TypeSymbol toType, boolean secondIsClass,
                                    boolean allowDownCast, ISymbol assigned) throws UndeclaredException,
            IllegalCastAssignmentException {

        String where = context.getMemberName();
        String name1 = type.getTypeDclNode().fullName;
        String name2 = toType.getTypeDclNode().fullName;

        if (assigned instanceof INumericLiteral && SizeHelper.maxValues.containsKey(toType.value)) {
            INumericLiteral numeric = (INumericLiteral) assigned;
            long val = numeric.getAsLongValue();
            long max = SizeHelper.maxValues.get(toType.value);
            if (toType.isArray || (type.value.equals(JoosNonTerminal.LONG) && !toType.value.equals(JoosNonTerminal.LONG)) ||
                    val > max || val < 0 && JoosNonTerminal.unsigned.contains(toType.value) || val < -max - 1) {
                throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
            }
            return;
        }

        Castable castType = toType.getTypeDclNode().getCastablility(type.getTypeDclNode());

        if (castType == Castable.NOT_CASTABLE || toType.isClass != secondIsClass
                || (castType == Castable.DOWN_CAST && !allowDownCast)
                || type.value.equals(JoosNonTerminal.VOID) || toType.value.equals(JoosNonTerminal.VOID)
                || (!type.value.equals(toType.value) && type.isArray && JoosNonTerminal.primativeNumbers.contains(toType.value))) {
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }
    }

    @Override
    public void visit(CastExpressionSymbol symbol) throws CompilerException {
        castOrAssign(true, true);
        //cast is to a class, make sure it's not a class after the cast
        Deque<Typeable> currentDeque = currentTypes.peek();
        TypeSymbol type = currentDeque.removeLast().getType();
        TypeSymbol finalType = type.getNonClassVersion();
        currentDeque.add(finalType);
    }

    @Override
    public void close(ReturnExprSymbol returnSymbol) throws IllegalCastAssignmentException, UndeclaredException {
        TypeSymbol currentType;
        if (!returnSymbol.children.isEmpty()) {
            currentType = currentTypes.peek().getLast().getType();
        } else {
            currentType = TypeSymbol.getPrimative(JoosNonTerminal.VOID);
        }
        returnSymbol.setType(currentType);
        if (context.getCurrentMember().type.getTypeDclNode().getCastablility(currentType.getTypeDclNode()) != Castable.UP_CAST || currentType.isClass
                || (context.getCurrentMember().type.value == JoosNonTerminal.VOID && !returnSymbol.children.isEmpty())) {
            String where = context.getMemberName();

            String name1 = currentType.isArray ? ArrayPkgClassResolver.getArrayName(currentType.value) :
                    currentType.value;
            String name2 = context.getCurrentMember().type.isArray ?
                    ArrayPkgClassResolver.getArrayName(context.getCurrentMember().type.value) :
                    context.getCurrentMember().type.value;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }
    }

    @Override
    public void visit(TypeSymbol type) {
        currentTypes.peek().add(type);
    }

    @Override
    public void visit(AssignmentExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException,
            UnsupportedException {
        Typeable leftHS = currentTypes.peek().removeLast();
        if (leftHS.getType().isFinal) {
            throw new UnsupportedException("left hand side of assignment cannot be final.");
        }

        TypeSymbol rightHSType = currentTypes.peek().removeLast().getType();
        assertIsAssignable(rightHSType, leftHS.getType(), false, false, op.children.get(1));

        op.setType(leftHS.getType());
        currentTypes.peek().add(leftHS);
    }

    private void eqNeHelper(BinOpExpr op) throws IllegalCastAssignmentException, UndeclaredException,
            BadOperandsTypeException {
        TypeSymbol isType = currentTypes.peek().removeLast().getType();
        TypeSymbol toType = currentTypes.peek().getLast().getType();

        Castable castType = toType.getTypeDclNode().getCastablility(isType.getTypeDclNode());

        if (castType == Castable.NOT_CASTABLE || toType.isClass
                || isType.value.equals(JoosNonTerminal.VOID) || toType.value.equals(JoosNonTerminal.VOID)
                || (!isType.value.equals(toType.value) && isType.isArray && JoosNonTerminal.primativeNumbers.contains(toType.value))
                || (JoosNonTerminal.primativeNumbers.contains(isType.value) && !JoosNonTerminal.primativeNumbers.contains(toType.value))
                || (!JoosNonTerminal.primativeNumbers.contains(isType.value) && JoosNonTerminal.primativeNumbers.contains(toType.value))) {
            String where = context.getMemberName();
            String name1 = isType.getTypeDclNode().fullName;
            String name2 = toType.getTypeDclNode().fullName;
            throw new IllegalCastAssignmentException(context.enclosingClassName, where, name1, name2);
        }

        currentTypes.peek().removeLast();
        TypeSymbol boolType = TypeSymbol.getPrimative(JoosNonTerminal.BOOLEAN);
        op.setType(boolType);
        currentTypes.peek().add(boolType);
    }

    @Override
    public void visit(EqExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException,
            BadOperandsTypeException {
        eqNeHelper(op);
    }

    @Override
    public void visit(NeExprSymbol op) throws IllegalCastAssignmentException, UndeclaredException,
            BadOperandsTypeException {
        eqNeHelper(op);
    }

    @Override
    public void visit(LtExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.BOOLEAN, op);
    }

    @Override
    public void visit(GtExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.BOOLEAN, op);
    }

    @Override
    public void visit(LeExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.BOOLEAN, op);
    }

    @Override
    public void visit(GeExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.BOOLEAN, op);
    }

    @Override
    public void visit(AddExprSymbol op) throws BadOperandsTypeException, UndeclaredException {
        TypeSymbol second = currentTypes.peek().removeLast().getType();
        TypeSymbol first = currentTypes.peek().removeLast().getType();

        if ((isCastable(first, JoosNonTerminal.STRING, false) && !second.value.equals(JoosNonTerminal.VOID))
                || (isCastable(second, JoosNonTerminal.STRING, false) && !first.value.equals(JoosNonTerminal.VOID))) {
            TypeSymbol strType = TypeSymbol.getPrimative(JoosNonTerminal.STRING);
            op.setType(strType);
            currentTypes.peek().add(strType);
        } else {
            isNumeric(first, true);
            isNumeric(second, true);

            TypeSymbol type = TypeSymbol.getPrimative(JoosNonTerminal.INTEGER);
            if (first.value.equals(JoosNonTerminal.LONG) || second.value.equals(JoosNonTerminal.LONG)) {
                type = TypeSymbol.getPrimative(JoosNonTerminal.LONG);
            }

            op.setType(type);
            currentTypes.peek().add(type);
        }
    }

    @Override
    public void visit(SubtractExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    public void shiftHelper(BinOpExpr op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
        Typeable typeable = (Typeable) op.children.get(1);
        TypeSymbol rhs = typeable.getType();
        TypeSymbol ltype = TypeSymbol.getPrimative(JoosNonTerminal.LONG);

        if (rhs == ltype) {
            String where = context.getMemberName();
            throw new BadOperandsTypeException(context.enclosingClassName, where, JoosNonTerminal.LONG,
                    JoosNonTerminal.INTEGER);
        }
    }

    @Override
    public void visit(LSExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        shiftHelper(op);
    }

    @Override
    public void visit(RSExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        shiftHelper(op);
    }

    @Override
    public void visit(URSExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        shiftHelper(op);
    }

    @Override
    public void visit(MultiplyExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void visit(DivideExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void visit(RemainderExprSymbol op) throws UndeclaredException, BadOperandsTypeException {
        bothIntHelper(JoosNonTerminal.INTEGER, op);
    }

    @Override
    public void open(CreationExpression create) {
        useCurrentScopeForLookup.push(false);
        currentTypes.push(new ArrayDeque<Typeable>());
    }

    @Override
    public void close(CreationExpression create) throws CompilerException {
        useCurrentScopeForLookup.pop();
        Deque<Typeable> currentSymbols = currentTypes.pop();
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(context.enclosingClassName);
        TypeSymbol typeSymbol = create.getType();
        resolver = resolver.getClass(typeSymbol.value, true);
        if (typeSymbol.isArray) {
            resolver = resolver.getArrayVersion();
        }

        if (resolver.isAbstract()) {
            throw new CompilerException(context.enclosingClassName,
                    context.getMemberName(),
                    "cannot instantiate abstract type " + typeSymbol.value);
        }

        List<String> params = new LinkedList<String>();

        currentSymbols.pop();

        for (Typeable mod : currentSymbols) {
            TypeSymbol type = mod.getType();
            //If it's a class this would be like the argument is String.  There is no local String.
            if (type.isClass) {
                throw new UndeclaredException("Class arguments", context.getMemberName());
            }

            String name = type.getTypeDclNode().fullName;
            params.add(name);
        }

        if (!resolver.isBuilt) {
            resolver.build();
        }
        resolver.getConstructor(params, PkgClassInfo.instance.getSymbol(context.enclosingClassName));
        create.setType(typeSymbol);
        currentTypes.peek().add(create);
    }

    @Override
    public void visit(ArrayAccessExprSymbol array) throws CompilerException {
        TypeSymbol value = currentTypes.peek().removeLast().getType();
        TypeSymbol in = currentTypes.peek().removeLast().getType();

        if (in.isClass) {
            throw new UnsupportedException("Array access for classes in " + context.getCurrentMember().dclName + " " + context.enclosingClassName);
        }

        if (!isNumeric(value, false)) {
            throw new UnsupportedException("Array access with non numeric " + value.getType().value + " " + context.getCurrentMember().dclName + " " + context.enclosingClassName);
        }

        TypeSymbol retType = new TypeSymbol(in.value, false, false);
        retType.setTypeDclNode(in.getTypeDclNode().accessor());
        array.setType(retType);
        currentTypes.peek().add(retType);
    }

    private void bothIntHelper(String returnType, BinOpExpr op) throws BadOperandsTypeException,
            UndeclaredException {
        TypeSymbol second = currentTypes.peek().removeLast().getType();
        currentTypes.peek().getLast();
        TypeSymbol first = currentTypes.peek().removeLast().getType();
        isNumeric(first, true);
        isNumeric(second, true);

        TypeSymbol type = TypeSymbol.getPrimative(returnType);
        if (returnType.equals(JoosNonTerminal.INTEGER) &&
                (first.value.equals(JoosNonTerminal.LONG) || second.value.equals(JoosNonTerminal.LONG))) {

            type = TypeSymbol.getPrimative(JoosNonTerminal.LONG);
        }

        op.setType(type);
        currentTypes.peek().add(type);
    }

    private boolean isNumeric(TypeSymbol type, boolean die)
            throws UndeclaredException, BadOperandsTypeException {

        return isCastable(type, JoosNonTerminal.INTEGER, die);
    }

    private void assertIsBoolean(TypeSymbol type) throws UndeclaredException,
            BadOperandsTypeException {

        if (type.isArray || type.isClass) {
            String where = context.getMemberName();
            throw new BadOperandsTypeException(context.enclosingClassName, where,
                    ArrayPkgClassResolver.getArrayName(type.value), context.getCurrentMember().type.value);
        }

        if (!type.value.equals(JoosNonTerminal.BOOLEAN)) {
            String where = context.getMemberName();
            throw new BadOperandsTypeException(context.enclosingClassName, where, type.value,
                    context.getCurrentMember().type.value);
        }
    }

    private boolean isCastable(TypeSymbol type, String to, boolean die)
            throws UndeclaredException, BadOperandsTypeException {

        if (type.isArray || type.isClass) {
            String where = context.getMemberName();
            if (die) {
                throw new BadOperandsTypeException(context.enclosingClassName, where,
                        ArrayPkgClassResolver.getArrayName(type.value), context.getCurrentMember().type.value);
            }
            return false;
        }

        APkgClassResolver typeResolver = PkgClassInfo.instance.getSymbol(to);
        if (typeResolver == null) {
            throw new UndeclaredException(to, context.getMemberName());
        }
        if (typeResolver.getCastablility(type.getTypeDclNode()) == Castable.NOT_CASTABLE) {
            String where = context.getMemberName();
            if (die) {
                throw new BadOperandsTypeException(context.enclosingClassName, where, type.value,
                        context.getCurrentMember().type.value);
            }
            return false;
        }
        return true;
    }
}
