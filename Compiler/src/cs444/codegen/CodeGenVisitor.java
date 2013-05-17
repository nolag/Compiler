package cs444.codegen;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.instructions.x86.Add;
import cs444.codegen.instructions.x86.Call;
import cs444.codegen.instructions.x86.Cmp;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.Global;
import cs444.codegen.instructions.x86.Int;
import cs444.codegen.instructions.x86.Je;
import cs444.codegen.instructions.x86.Jg;
import cs444.codegen.instructions.x86.Jge;
import cs444.codegen.instructions.x86.Jmp;
import cs444.codegen.instructions.x86.Jne;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.Leave;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Movsx;
import cs444.codegen.instructions.x86.Movzx;
import cs444.codegen.instructions.x86.Neg;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.ReserveInstruction;
import cs444.codegen.instructions.x86.Ret;
import cs444.codegen.instructions.x86.Sar;
import cs444.codegen.instructions.x86.Section;
import cs444.codegen.instructions.x86.Section.SectionType;
import cs444.codegen.instructions.x86.Shl;
import cs444.codegen.instructions.x86.Xor;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.AddOpMaker;
import cs444.codegen.instructions.x86.factories.AndOpMaker;
import cs444.codegen.instructions.x86.factories.BinOpMaker;
import cs444.codegen.instructions.x86.factories.CmpMaker;
import cs444.codegen.instructions.x86.factories.IDivMaker;
import cs444.codegen.instructions.x86.factories.IMulMaker;
import cs444.codegen.instructions.x86.factories.OrOpMaker;
import cs444.codegen.instructions.x86.factories.ReserveInstructionMaker;
import cs444.codegen.instructions.x86.factories.SeteMaker;
import cs444.codegen.instructions.x86.factories.SetlMaker;
import cs444.codegen.instructions.x86.factories.SetleMaker;
import cs444.codegen.instructions.x86.factories.SetneMaker;
import cs444.codegen.instructions.x86.factories.SubOpMaker;
import cs444.codegen.instructions.x86.factories.UniOpMaker;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.PointerRegister;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.StaticFieldInit;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.codegen.x86_32.linux.X86_32LinuxPlatform;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ImplementationLevel;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ProtectionLevel;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;
import cs444.parser.symbols.ast.ByteLiteralSymbol;
import cs444.parser.symbols.ast.CharacterLiteralSymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.EmptyStatementSymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.IntegerLiteralSymbol;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.parser.symbols.ast.NullSymbol;
import cs444.parser.symbols.ast.ShortLiteralSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.parser.symbols.ast.SuperSymbol;
import cs444.parser.symbols.ast.ThisSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
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
import cs444.types.APkgClassResolver;
import cs444.types.ArrayPkgClassResolver;
import cs444.types.PkgClassInfo;
import cs444.types.PkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

public class CodeGenVisitor{
    private static final String INIT_OBJECT_FUNC = "__init_object";
    public static final String NATIVE_NAME = "NATIVE";

    //TODO make this any type of platform when I have tiles
    //private final IPlatform<?> platform;
    private final X86_32LinuxPlatform platform;

    private boolean hasEntry = false;
    private boolean getVal = true;

    private boolean lastWasFunc = false;

    private Size lastSize = Size.DWORD;
    private long nextLblnum = 0;
    private APkgClassResolver currentFile;
    private boolean isSuper = false;

    final InstructionHolder<X86Instruction> instructions;
    final X86SizeHelper sizeHelper;

    private long getNewLblNum(){
        return nextLblnum++;
    }

    public CodeGenVisitor(final IPlatform<?> platform) {
        this(null, platform);
    }

    public CodeGenVisitor(final APkgClassResolver resolver, final IPlatform<?> platform) {
        this.platform = (X86_32LinuxPlatform)platform;
        this.currentFile = resolver;
        this.instructions = this.platform.getInstructionHolder();
        this.sizeHelper = this.platform.getSizeHelper();
    }

    public void genHeader(final APkgClassResolver resolver) {
        this.currentFile = resolver;

        Runtime.instance.externAll(instructions);
        instructions.add(new Section(SectionType.TEXT));

        instructions.add(new Comment(INIT_OBJECT_FUNC + ": call super default constructor and initialize obj fields." +
                " eax should contain address of object."));
        instructions.add(new Label(INIT_OBJECT_FUNC));

        APkgClassResolver superResolver = null;

        superResolver = resolver.getSuper();

        if (!resolver.fullName.equals(JoosNonTerminal.OBJECT)){
            invokeConstructor(superResolver, Collections.<ISymbol>emptyList());
        }

        instructions.add(new Comment("Store pointer to object in edx"));
        instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, sizeHelper));

        for (final DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            final Size size = X86SizeHelper.getSize(fieldDcl.getType().getTypeDclNode().realSize);

            final PointerRegister fieldAddr = new PointerRegister(Register.DATA, fieldDcl.getOffset());
            if(!fieldDcl.children.isEmpty()){
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));
                // save pointer to object
                instructions.add(new Push(Register.DATA, sizeHelper));
                final CodeGenVisitor visitor = new CodeGenVisitor(currentFile, platform);
                fieldDcl.children.get(0).accept(visitor);
                instructions.add(new Comment("Pop the object address to edx"));
                instructions.add(new Pop(Register.DATA, sizeHelper));
                instructions.add(new Mov(fieldAddr, Register.ACCUMULATOR, size, sizeHelper));
            }
        }

        instructions.add(Ret.RET);
    }

    public void genLayoutForStaticFields(final Iterable<DclSymbol> staticFields) {
        if (staticFields.iterator().hasNext()){
            instructions.add(new Comment("Static fields:"));
            instructions.add(new Section(SectionType.BSS));
        }

        for (final DclSymbol fieldDcl : staticFields) {
            final Size size = X86SizeHelper.getSize(fieldDcl.getType().getTypeDclNode().realSize);

            final String fieldLbl = APkgClassResolver.getUniqueNameFor(fieldDcl);
            instructions.add(new Global(fieldLbl));
            final ReserveInstruction data = ReserveInstructionMaker.make(fieldLbl, size, 1);
            instructions.add(data);
        }
    }

    public void visit(final FieldAccessSymbol field) {
        final boolean wasGetVal = getVal;
        final boolean wasSuper = isSuper;
        getVal = true;
        field.children.get(0).accept(this);
        ifNullJmpCode(Register.ACCUMULATOR, Runtime.EXCEPTION_LBL);
        //super.x().y() x should be from super but not y
        getVal = wasGetVal;
        if(wasSuper) isSuper = false;
        field.children.get(1).accept(this);
        isSuper = false;

    }

    public void visit(final AInterfaceOrClassSymbol aInterfaceOrClassSymbol) {
        for(final ISymbol child : aInterfaceOrClassSymbol.children) child.accept(this);
    }

    public void visit(final MethodSymbol method){
        final String methodName = APkgClassResolver.generateFullId(method);

        try{
            if(APkgClassResolver.generateUniqueName(method, method.dclName).equals(JoosNonTerminal.ENTRY)
                    && method.isStatic() && !hasEntry){

                hasEntry = true;
                instructions.add(new Global("_start"));
                instructions.add(new Label("_start"));
                instructions.add(new Extern(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL)));
                instructions.add(new Call(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL), sizeHelper));
                instructions.add(new Call(new Immediate(methodName), sizeHelper));
                instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
                instructions.add(new Mov(Register.ACCUMULATOR, Immediate.EXIT, sizeHelper));
                instructions.add(new Int(Immediate.SOFTWARE_INTERUPT, sizeHelper));
            }
        }catch(final Exception e){
            //Should never get here.
            e.printStackTrace();
        }

        if(method.isNative()){
            instructions.add(new Extern(methodName));
        }else{
            methProlog(method, methodName);
            for(final ISymbol child : method.children) child.accept(this);
            methEpilogue(method);
        }
    }

    public void visit(final ConstructorSymbol constructor) {
        final String constrName = APkgClassResolver.generateFullId(constructor);
        methProlog(constructor, constrName);

        instructions.add(new Mov(Register.ACCUMULATOR, PointerRegister.THIS));

        instructions.add(new Call(new Immediate(INIT_OBJECT_FUNC), sizeHelper));

        for(final ISymbol child : constructor.children) child.accept(this);

        methEpilogue(constructor);
    }

    private void methProlog(final MethodOrConstructorSymbol method, final String methodName) {
        if(method.getProtectionLevel() != ProtectionLevel.PRIVATE) instructions.add(new Global(methodName));
        instructions.add(new Label(methodName));

        lastWasFunc = true;

        instructions.add(Push.getStackPush(sizeHelper));
        instructions.add(new Mov(Register.FRAME, Register.STACK, sizeHelper));
    }

    private void methEpilogue(final MethodOrConstructorSymbol method) {
        //Don't fall though void funcs
        instructions.add(Leave.LEAVE);
        instructions.add(Ret.RET);

        instructions.add(new Comment("End of method " + method.dclName));
    }

    public void visit(final CreationExpression creationExpression) {
        final APkgClassResolver typeDclNode = creationExpression.getType().getTypeDclNode();

        if (!creationExpression.getType().isArray){
            final long allocSize = typeDclNode.getStackSize(platform);
            final InstructionArg bytes = new Immediate(String.valueOf(allocSize));
            instructions.add(new Comment("Allocate " + bytes.getValue(sizeHelper) + " bytes for " + typeDclNode.fullName));
            instructions.add(new Mov(Register.ACCUMULATOR, bytes, sizeHelper));

            platform.getRunime().mallocClear(instructions);

            platform.getObjectLayout().initialize(typeDclNode, instructions);

            final APkgClassResolver resolver = creationExpression.getType().getTypeDclNode();

            final List<ISymbol> children = creationExpression.children;

            instructions.add(new Comment("invoke Constructor"));
            invokeConstructor(resolver, children);
        }else{
            instructions.add(new Comment("Getting size for array constuction"));
            creationExpression.children.get(0).accept(this);
            instructions.add(new Comment("Save the size of the array"));
            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

            instructions.add(new Comment("Checking array size >= 0"));
            final String ok = "arrayCreateOk" + getNewLblNum();
            instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
            instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));

            instructions.add(new Jge(new Immediate(ok), sizeHelper));
            Runtime.instance.throwException(instructions, "Invalid array creation");
            instructions.add(new Label(ok));

            if(lastSize != Size.LOW && lastSize != Size.HIGH)
                instructions.add(new Shl(Register.ACCUMULATOR, X86SizeHelper.getPowerSizeImd(lastSize), sizeHelper));

            instructions.add(new Comment("Adding space for SIT, cast info, and length" + typeDclNode.fullName));
            //Int + object's sie
            final long baseSize = platform.getObjectLayout().objSize() + X86SizeHelper.getIntSize(Size.DWORD);
            final Immediate sizeI = new Immediate(String.valueOf(baseSize));
            instructions.add(new Add(Register.ACCUMULATOR, sizeI, sizeHelper));
            instructions.add(new Comment("Allocate for array" + typeDclNode.fullName));
            platform.getRunime().mallocClear(instructions);
            instructions.add(new Comment("Pop the size"));
            instructions.add(new Pop(Register.DATA, sizeHelper));

            final long lengthIndex = platform.getObjectLayout().objSize();
            final Immediate li = new Immediate(String.valueOf(lengthIndex));

            instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR, li), Register.DATA, sizeHelper));
            platform.getObjectLayout().initialize(typeDclNode, instructions);
        }
        lastSize = Size.DWORD;
        instructions.add(new Comment("Done creating object"));
    }

    private void invokeConstructor(final APkgClassResolver resolver, final List<ISymbol> children) {
        final List<String> types = new LinkedList<String>();

        instructions.add(new Comment("Back up addr of obj in Base so it is safe"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        for(final ISymbol child : children){
            child.accept(this);
            instructions.add(new Push(Register.ACCUMULATOR, X86SizeHelper.getPushSize(lastSize), sizeHelper));
            final Typeable typeable = (Typeable) child;
            final TypeSymbol ts = typeable.getType();
            types.add(ts.getTypeDclNode().fullName);
        }

        instructions.add(new Push(Register.BASE, sizeHelper));

        ConstructorSymbol cs = null;
        try {
            cs = resolver.getConstructor(types, resolver);
        } catch (final UndeclaredException e) {
            //Should never get here
            e.printStackTrace();
        }

        final Immediate arg = new Immediate(APkgClassResolver.generateFullId(cs));
        if(cs.dclInResolver != currentFile) instructions.add(new Extern(arg));
        instructions.add(new Call(arg, sizeHelper));
        //return value is the new object
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        final long mySize = cs.getStackSize() - sizeHelper.defaultStackSize;
        if(mySize != 0){
            final Immediate by = new Immediate(String.valueOf(mySize));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }
        instructions.add(new Comment("Restore Base register"));
        instructions.add(new Pop(Register.BASE, sizeHelper));
    }

    public void visit(final ANonTerminal aNonTerminal) {
        final boolean isBlock = aNonTerminal.getName().equals(JoosNonTerminal.BLOCK);
        final boolean lastFunc = lastWasFunc;
        lastWasFunc = false;

        for(final ISymbol child : aNonTerminal.children) child.accept(this);

        if(isBlock && !lastFunc){
            final long size = aNonTerminal.getStackSize();
            if(0 != size){
                final Immediate by = new Immediate(String.valueOf(size));
                instructions.add(new Add(Register.STACK, by, sizeHelper));
            }
        }
        lastWasFunc = lastFunc;
    }

    public void visit(final WhileExprSymbol whileExprSymbol) {
        final long mynum = getNewLblNum();
        instructions.add(new Comment("while start " + mynum));
        final String loopStart = "loopStart" + mynum;
        final String loopEnd = "loopEnd" + mynum;

        instructions.add(new Label(loopStart));
        whileExprSymbol.getConditionSymbol().accept(this);

        setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, loopEnd);

        whileExprSymbol.getBody().accept(this);

        instructions.add(new Jmp(new Immediate(loopStart), sizeHelper));
        instructions.add(new Label(loopEnd));
        instructions.add(new Comment("while end " + mynum));
    }

    public void visit(final ForExprSymbol forExprSymbol) {
        final long mynum = getNewLblNum();
        instructions.add(new Comment("for start " + mynum));
        final String loopStart = "loopStart" + mynum;
        final String loopEnd = "loopEnd" + mynum;

        instructions.add(new Comment("Init for " + mynum));
        forExprSymbol.getForInit().accept(this);
        instructions.add(new Label(loopStart));
        instructions.add(new Comment("Compare for " + mynum));
        forExprSymbol.getConditionExpr().accept(this);

        setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, loopEnd);

        instructions.add(new Comment("for body" + mynum));

        forExprSymbol.getBody().accept(this);

        instructions.add(new Comment("for update " + mynum));
        forExprSymbol.getForUpdate().accept(this);

        instructions.add(new Jmp(new Immediate(loopStart), sizeHelper));
        instructions.add(new Label(loopEnd));

        //This takes care of the init if they dcl something there
        final long size = forExprSymbol.getStackSize();
        if(0 != size){
            final Immediate by = new Immediate(String.valueOf(size));
            instructions.add(new Comment("for stack " + mynum));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }

        instructions.add(new Comment("for end " + mynum));
    }

    public void visit(final IfExprSymbol ifExprSymbol) {
        final long myid = getNewLblNum();
        instructions.add(new Comment("if start" + myid));
        final String falseLbl = "false" + myid;
        final String trueLbl = "true" + myid;
        ifExprSymbol.getConditionSymbol().accept(this);

        setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, falseLbl);

        ifExprSymbol.getifBody().accept(this);

        instructions.add(new Jmp(new Immediate(trueLbl), sizeHelper));
        instructions.add(new Label(falseLbl));

        final ISymbol elseSymbol = ifExprSymbol.getElseBody();

        if(elseSymbol != null) elseSymbol.accept(this);

        instructions.add(new Label(trueLbl));
        instructions.add(new Comment("if end" + myid));
    }

    public void visit(final ReturnExprSymbol retSymbol) {
        if(retSymbol.children.size() == 1) retSymbol.children.get(0).accept(this);
        //value is already in eax from the rule therefore we just need to ret
        instructions.add(Leave.LEAVE);
        instructions.add(Ret.RET);
    }

    private void genMov(final Size size, final InstructionArg from, final String value, final Typeable dcl){
        X86Instruction instruction;

        if(size == Size.DWORD){
            instruction = new Mov(Register.ACCUMULATOR, from, sizeHelper);
        }else if(JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)){
            instruction = new Movzx(Register.ACCUMULATOR, from, size, sizeHelper);
        }else{
            instruction = new Movsx(Register.ACCUMULATOR, from, size, sizeHelper);
        }

        instructions.add(new Comment("getting value of " + value));
        instructions.add(instruction);
    }

    public void visit(final CastExpressionSymbol symbol) {
        final TypeSymbol type = symbol.getType();

        symbol.getOperandExpression().accept(this);

        final String typeName = type.getTypeDclNode().fullName;
        if(isReferenceType(typeName)){
            final String castExprEnd = "CastExprEnd" + getNewLblNum();
            ifNullJmpCode(Register.ACCUMULATOR, castExprEnd);

            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
            platform.getObjectLayout().subtypeCheckCode(type, platform);
            setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, Runtime.EXCEPTION_LBL);
            instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

            instructions.add(new Label(castExprEnd));

        }else{ // Primitive casting:
            final Size curSize = X86SizeHelper.getSize(type.getTypeDclNode().realSize);
            genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type);
        }

        //We will move to make the size default so that the default is always stored in the register
        lastSize = sizeHelper.defaultStack;
    }

    private boolean isReferenceType(final String typeName) {
        return !JoosNonTerminal.primativeNumbers.contains(typeName)
                && !JoosNonTerminal.otherPrimatives.contains(typeName);
    }

    public void visit(final NegOpExprSymbol op) {
        op.children.get(0).accept(this);
        instructions.add(new Neg(Register.ACCUMULATOR));
    }

    public void visit(final NotOpExprSymbol op) {
        op.children.get(0).accept(this);
        instructions.add(new Xor(Register.ACCUMULATOR, Immediate.TRUE, sizeHelper));
    }

    public void visit(final MultiplyExprSymbol op) {
        binUniOpHelper(op, IMulMaker.maker, false);
    }

    public void visit(final AssignmentExprSymbol op) {
        final ISymbol leftHandSide = op.children.get(0);
        final ISymbol rightHandSide = op.children.get(1);

        instructions.add(new Comment("Start Assignment " + leftHandSide.getName() + "="
                + rightHandSide.getName()));
        // LHS
        final boolean tmp = getVal;
        getVal = false;
        leftHandSide.accept(this);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        final Size LHSSize = lastSize;

        getVal = tmp;
        rightHandSide.accept(this);

        instructions.add(new Pop(Register.DATA, sizeHelper));

        final InstructionArg to = new PointerRegister(Register.DATA);
        instructions.add(new Mov(to, Register.ACCUMULATOR, LHSSize, sizeHelper));
        instructions.add(new Comment("End Assignment"));
    }

    public void visit(final DivideExprSymbol op) {
        instructions.add(new Comment("START"));
        binUniOpHelper(op, IDivMaker.maker, true);
        instructions.add(new Comment("END"));
    }

    public void visit(final RemainderExprSymbol op) {
        binUniOpHelper(op, IDivMaker.maker, true);
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, sizeHelper));
    }

    private void strPartHelper(final ISymbol child, final APkgClassResolver resolver){
        final String firstType = ((Typeable)child).getType().getTypeDclNode().fullName;
        child.accept(this);
        AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(firstType), isSuper);
        if(ms == null) ms = resolver.safeFindMethod(JoosNonTerminal.TO_STR, true, Arrays.asList(JoosNonTerminal.OBJECT), isSuper);

        lastSize = X86SizeHelper.getPushSize(lastSize);
        final int pop = X86SizeHelper.getIntSize(lastSize);

        instructions.add(new Push(Register.ACCUMULATOR, lastSize, sizeHelper));

        final Immediate arg = new Immediate(APkgClassResolver.generateFullId(ms));
        if(ms.dclInResolver != currentFile) instructions.add(new Extern(arg));
        instructions.add(new Call(arg, sizeHelper));

        instructions.add(new Add(Register.STACK, new Immediate(pop), sizeHelper));
    }

    public void visit(final AddExprSymbol op) {
        if(op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.STRING)){
            final APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.STRING);
            final ISymbol firstChild = op.children.get(0);
            final ISymbol secondChild = op.children.get(1);

            instructions.add(new Comment("String add first arg"));
            strPartHelper(firstChild, resolver);
            instructions.add(new Comment("Backup first string"));
            instructions.add(new Push(Register.BASE, sizeHelper));
            instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

            instructions.add(new Comment("String add second arg"));
            strPartHelper(secondChild, resolver);

            instructions.add(new Comment("Pushing second string as arument then first as this"));
            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Push(Register.BASE, sizeHelper));
            final AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.STR_ADD, false, Arrays.asList(JoosNonTerminal.STRING), isSuper);
            final Immediate arg = new Immediate(APkgClassResolver.generateFullId(ms));
            if(ms.dclInResolver != currentFile) instructions.add(new Extern(arg));
            instructions.add(new Call(arg, sizeHelper));

            //The two arguments for string concat
            instructions.add(new Add(Register.STACK, new Immediate(sizeHelper.defaultStackSize * 2), sizeHelper));
            instructions.add(new Pop(Register.BASE, sizeHelper));
            instructions.add(new Comment("end of string add"));
            lastSize = X86SizeHelper.getSize(sizeHelper.defaultStackSize);
        }else{
            binOpHelper(op, AddOpMaker.maker);
        }
    }

    public void visit(final SubtractExprSymbol op) {
        binOpHelper(op, SubOpMaker.maker);
    }

    public void visit(final LtExprSymbol op) {
        compHelper(op, SetlMaker.maker);
    }

    public void visit(final EqExprSymbol op) {
        compHelper(op, SeteMaker.maker);
    }

    public void visit(final NeExprSymbol op) {
        compHelper(op, SetneMaker.maker);

    }

    public void visit(final AndExprSymbol op) {
        final String andEnd = "and" + getNewLblNum();
        op.children.get(0).accept(this);
        setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, andEnd);
        op.children.get(1).accept(this);
        setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, andEnd);
        instructions.add(new Label(andEnd));
    }

    public void visit(final OrExprSymbol op) {
        final String orEnd = "or" + getNewLblNum();
        op.children.get(0).accept(this);
        setupJumpNe(Register.ACCUMULATOR, Immediate.FALSE, orEnd);
        op.children.get(1).accept(this);
        setupJumpNe(Register.ACCUMULATOR, Immediate.FALSE, orEnd);
        instructions.add(new Label(orEnd));
    }

    public void visit(final EAndExprSymbol op) {
        binOpHelper(op, AndOpMaker.maker);
    }

    public void visit(final EOrExprSymbol op) {
        binOpHelper(op, OrOpMaker.maker);
    }

    public void visit(final LeExprSymbol op) {
        compHelper(op, SetleMaker.maker);
    }

    public void visit(final InstanceOfExprSymbol op) {
        op.getLeftOperand().accept(this);
        // eax should have reference to object
        final String nullObjectLbl = "nullObject" + getNewLblNum();
        ifNullJmpCode(Register.ACCUMULATOR, nullObjectLbl);

        platform.getObjectLayout().subtypeCheckCode((TypeSymbol) op.getRightOperand(), platform);

        final String endLbl = "instanceOfEnd" + getNewLblNum();
        instructions.add(new Jmp(new Immediate(endLbl), sizeHelper));

        instructions.add(new Label(nullObjectLbl));
        instructions.add(new Comment("set eax to FALSE"));
        instructions.add(new Mov(Register.ACCUMULATOR, Immediate.FALSE, sizeHelper));

        instructions.add(new Label(endLbl));
    }

    private void ifNullJmpCode(final Register register, final String ifNullLbl) {
        instructions.add(new Comment("null check"));
        instructions.add(new Cmp(register, Immediate.NULL, sizeHelper));
        instructions.add(new Je(new Immediate(ifNullLbl), sizeHelper));
    }

    public void visit(final IntegerLiteralSymbol intLiteral) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(intLiteral.getValue()), sizeHelper));
        lastSize = Size.DWORD;
    }

    public void visit(final NullSymbol nullSymbol) {
        instructions.add(new Mov(Register.ACCUMULATOR, Immediate.NULL, sizeHelper));
        lastSize = Size.DWORD;
    }

    public void visit(final BooleanLiteralSymbol boolSymbol) {
        instructions.add(new Mov(Register.ACCUMULATOR, boolSymbol.boolValue ? Immediate.TRUE : Immediate.FALSE, sizeHelper));
        lastSize = Size.WORD;
    }

    public void visit(final ThisSymbol thisSymbol) {
        instructions.add(new Comment("This pointer"));
        instructions.add(new Mov(Register.ACCUMULATOR, PointerRegister.THIS, sizeHelper));
    }

    public void visit(final SuperSymbol superSymbol) {
        instructions.add(new Comment("This (super) pointer"));
        instructions.add(new Mov(Register.ACCUMULATOR, PointerRegister.THIS, sizeHelper));
        isSuper = true;
    }

    public void visit(final StringLiteralSymbol stringSymbol) {
        instructions.add(new Comment("allocate the string at the same time (why not)"));
        //2 per char + dword for int + obj size
        final long charsLen = stringSymbol.strValue.length() * 2 + X86SizeHelper.getIntSize(Size.DWORD) + platform.getObjectLayout().objSize();
        final long length =  charsLen + stringSymbol.getType().getTypeDclNode().getStackSize(platform);

        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(length), sizeHelper));
        //no need to zero out, it will be set for sure so the second last arg does not matter
        Runtime.instance.mallocNoClear(instructions);
        final String charArray = ArrayPkgClassResolver.getArrayName(JoosNonTerminal.CHAR);
        platform.getObjectLayout().initialize(PkgClassInfo.instance.getSymbol(charArray), instructions);

        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR, 8), new Immediate(stringSymbol.strValue.length()), sizeHelper));

        final char [] cs = stringSymbol.strValue.toCharArray();
        for(int i = 0; i < cs.length; i++){
            final long place = 2 * i + platform.getObjectLayout().objSize() + X86SizeHelper.getIntSize(Size.DWORD);
            final InstructionArg to = new PointerRegister(Register.ACCUMULATOR, new Immediate(String.valueOf(place)));
            instructions.add(new Mov(to, new Immediate((cs[i])), Size.WORD, sizeHelper));
        }
        final APkgClassResolver resolver = stringSymbol.getType().getTypeDclNode();
        try {
            final String arg = charArray;
            final ConstructorSymbol constructor = resolver.getConstructor(Arrays.asList(arg), resolver);
            instructions.add(new Comment("First arg to new String"));
            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Comment("This pointer to new string"));
            instructions.add(new Add(Register.ACCUMULATOR, new Immediate(charsLen), sizeHelper));
            platform.getObjectLayout().initialize(resolver, instructions);
            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

            final Immediate carg = new Immediate(APkgClassResolver.generateFullId(constructor));
            if(constructor.dclInResolver != currentFile) instructions.add(new Extern(carg));

            instructions.add(new Call(carg, sizeHelper));
            instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Add(Register.STACK, new Immediate(sizeHelper.defaultStackSize), sizeHelper));

        } catch (final UndeclaredException e) {
            //Should never get here
            e.printStackTrace();
        }
        lastSize = Size.DWORD;
        instructions.add(new Comment("End of New String!"));
    }

    public void visit(final CharacterLiteralSymbol characterSymbol) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(characterSymbol.getValue()), sizeHelper));
        lastSize = Size.WORD;
    }

    public void visit(final ArrayAccessExprSymbol arrayAccess) {
        final boolean gettingValue = getVal;
        //always want the value of the array when accessing it's member.
        getVal = true;
        instructions.add(new Comment("Accessing array"));
        arrayAccess.children.get(0).accept(this);

        ifNullJmpCode(Register.ACCUMULATOR, Runtime.EXCEPTION_LBL);

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        arrayAccess.children.get(1).accept(this);

        instructions.add(new Comment("Checking element >= 0"));
        String ok = "arrayAccessOk" + getNewLblNum();
        instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));
        instructions.add(new Jge(new Immediate(ok), sizeHelper));
        Runtime.instance.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        ok = "arrayAccessOk" + getNewLblNum();
        final InstructionArg len = new PointerRegister(Register.BASE, new Immediate(platform.getObjectLayout().objSize()));
        instructions.add(new Cmp(len, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Jg(new Immediate(ok), sizeHelper));
        Runtime.instance.throwException(instructions, "Invalid array creation");
        instructions.add(new Label(ok));

        final long stackSize = arrayAccess.getType().getTypeDclNode().getStackSize(platform);
        Size elementSize;
        if(stackSize >= sizeHelper.defaultStackSize) elementSize = sizeHelper.defaultStack;
        else elementSize = X86SizeHelper.getSize(stackSize);

        instructions.add(new Shl(Register.ACCUMULATOR, Immediate.getImediateShift(X86SizeHelper.getPushSize(elementSize)), sizeHelper));
        final long offset = sizeHelper.defaultStackSize * 2 + X86SizeHelper.getIntSize(Size.DWORD);
        instructions.add(new Add(Register.ACCUMULATOR, new Immediate(offset), sizeHelper));

        if(gettingValue){
            getVal = true;
            genMov(elementSize, new PointerRegister(Register.ACCUMULATOR, Register.BASE), "array", arrayAccess);
            lastSize = elementSize;
        }else{
            instructions.add(new Add(Register.ACCUMULATOR, Register.BASE, sizeHelper));
            lastSize = X86SizeHelper.getSize(sizeHelper.defaultStackSize);
        }
        instructions.add(new Pop(Register.BASE, sizeHelper));
    }

    public void visit(final DclSymbol dclSymbol) {
        if(dclSymbol.children.isEmpty()) new IntegerLiteralSymbol(0).accept(this);
        else dclSymbol.children.get(0).accept(this);
        lastSize = X86SizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getStackSize(platform));
        instructions.add(new Push(Register.ACCUMULATOR, lastSize, sizeHelper));
    }

    public void visit(final ByteLiteralSymbol byteLiteral) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(byteLiteral.getValue()), sizeHelper));
        lastSize = Size.WORD;
    }

    public void visit(final ShortLiteralSymbol shortLiteral) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(shortLiteral.getValue()), sizeHelper));
        lastSize = Size.WORD;
    }

    public void printToFileAndEmpty(final PrintStream printer){
        instructions.passToNextClear(printer);
    }

    private void binOpHelper(final BinOpExpr bin, final BinOpMaker maker){
        instructions.add(new Push(Register.BASE, sizeHelper));

        bin.children.get(0).accept(this);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        bin.children.get(1).accept(this);
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(maker.make(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
    }

    private void compHelper(final BinOpExpr bin, final UniOpMaker uni){
        binOpHelper(bin, CmpMaker.maker);
        instructions.add(new Comment("Xor here CAN change the setl bit"));
        instructions.add(uni.make(Register.DATA, sizeHelper));
        instructions.add(new Comment("clear all bits in register"));
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, Size.LOW, sizeHelper));
    }

    private void binUniOpHelper(final BinOpExpr bin, final UniOpMaker maker, final boolean sar){
        instructions.add(new Push(Register.BASE, sizeHelper));

        bin.children.get(0).accept(this);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        bin.children.get(1).accept(this);

        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        // pop first operand
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        // first operand -> eax, second operand -> ebx
        if(sar){
            instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Sar(Register.DATA, Immediate.PREP_EDX, sizeHelper));
            final String safeDiv = "safeDiv" + getNewLblNum();
            setupJumpNe(Register.BASE, Immediate.ZERO, safeDiv);
            Runtime.instance.throwException(instructions, "Divide by zero");
            instructions.add(new Label(safeDiv));
        }

        instructions.add(maker.make(Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
    }

    private void setupJumpNe(final Register reg, final Immediate when, final String lblTo){
        instructions.add(new Cmp(reg, when, sizeHelper));
        instructions.add(new Jne(new Immediate(lblTo), sizeHelper));
    }

    public void visit(final SimpleMethodInvoke invoke) {
        final MethodOrConstructorSymbol call = invoke.call;
        if(!call.isStatic()){
            instructions.add(new Comment("Backing up ebx because having this in ecx is bad"));
            instructions.add(new Push(Register.BASE, sizeHelper));
            instructions.add(new Comment("Preping this"));
            instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        }

        if(call.isNative()){
            instructions.add(new Push(Register.BASE, sizeHelper));
        }

        instructions.add(new Comment("Pushing args"));
        for(final ISymbol arg : invoke.children){
            arg.accept(this);
            instructions.add(new Push(Register.ACCUMULATOR, X86SizeHelper.getPushSize(lastSize), sizeHelper));
        }

        if(!call.isStatic())instructions.add(new Push(Register.BASE, sizeHelper));

        if(call.isStatic() || call.getImplementationLevel() == ImplementationLevel.FINAL || isSuper){
            String name = APkgClassResolver.generateFullId(call);
            if(call.isNative()) name = NATIVE_NAME + name;
            final Immediate arg = new Immediate(name);
            if(call.dclInResolver != currentFile || call.isNative()) instructions.add(new Extern(arg));
            instructions.add(new Call(arg, sizeHelper));
        }else{
            instructions.add(new Comment("get SIT column"));
            instructions.add(new Mov(Register.BASE, new PointerRegister(Register.BASE), sizeHelper));

            PointerRegister methodAddr = null;
            try {
                final long by = platform.getSelectorIndex().getOffset(PkgClassResolver.generateUniqueName(call, call.dclName));
                methodAddr = new PointerRegister(Register.BASE, by);
            } catch (final UndeclaredException e) {
                // shouldn't get here
                e.printStackTrace();
            }
            instructions.add(new Mov(Register.BASE,  methodAddr, sizeHelper));
            instructions.add(new Call(Register.BASE, sizeHelper));
        }

        // NOTE: do not use INVOKE in here, invoke gets size from method,
        // but visitor may visit InvokeSymbol before MethodSymbol
        final long mySize =  call.getStackSize();
        if(mySize != 0){
            final Immediate by = new Immediate(String.valueOf(mySize));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }

        if(!call.isStatic() || call.isNative())instructions.add(new Pop(Register.BASE, sizeHelper));

        //Note: it is not the line below since anything smaller would have been extended when loaded into return register.
        //lastSize = SizeHelper.getSize(invoke.getType().getTypeDclNode().stackSize);
        lastSize = sizeHelper.defaultStack;
        instructions.add(new Comment("end invoke"));
    }

    public void visit(final SimpleNameSymbol name) {
        final DclSymbol dcl = name.dcl;
        final Size size = X86SizeHelper.getSize(dcl.getType().getTypeDclNode().realSize);
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;
        lastSize = X86SizeHelper.getSize(dcl.getType().getTypeDclNode().getStackSize(platform));

        if(dcl.isStatic() && dcl.dclInResolver != currentFile) instructions.add(new Extern(staticFieldLbl));

        if(getVal){
            InstructionArg base = Register.ACCUMULATOR;
            if(dcl.isLocal) base = Register.FRAME;
            else if(dcl.isStatic()) base = new Immediate(staticFieldLbl);

            final InstructionArg from = new PointerRegister(base, dcl.getOffset());
            genMov(size, from, dcl.dclName, dcl);
            return;
        }

        X86Instruction instruction = new Add(Register.ACCUMULATOR, new Immediate(dcl.getOffset()), sizeHelper);
        if(dcl.isStatic()){
            instruction = new Mov(Register.ACCUMULATOR, new Immediate(staticFieldLbl), sizeHelper);
        }else if(dcl.isLocal){
            instructions.add(new Comment("mov frame to accumulator because it is local"));
            instructions.add(new Mov(Register.ACCUMULATOR, Register.FRAME, sizeHelper));
        }

        instructions.add(new Comment("Move reference of " + dcl.dclName + " in to Accumulator"));
        instructions.add(instruction);
    }

    public void visit(final EmptyStatementSymbol empty){ }

    public void visit(final ISymbol other){
        throw new IllegalArgumentException(other.toString());
    }
}
