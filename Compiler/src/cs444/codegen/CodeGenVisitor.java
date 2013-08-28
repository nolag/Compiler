package cs444.codegen;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.expressions.*;
import cs444.types.APkgClassResolver;

public class CodeGenVisitor <T extends Instruction, E extends Enum<E>> {
    private static Map<Platform<?, ?>, CodeGenVisitor<?, ?>> codeGens = new HashMap<Platform<?, ?>, CodeGenVisitor<?, ?>>();

    //NOTE: this is for testing
    public static void reset(){
        codeGens = new HashMap<Platform<?, ?>, CodeGenVisitor<?, ?>>();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Instruction, E extends Enum<E>> CodeGenVisitor<T, E> getCurrentCodeGen(
            final Platform<T, E> platform){

        return (CodeGenVisitor<T, E>) codeGens.get(platform);
    }

    public static final String INIT_OBJECT_FUNC = "__init_object";
    public static final String NATIVE_NAME = "NATIVE";
    public final Platform<T, E> platform;
    private boolean hasEntry = false;
    public boolean getVal = true;
    public boolean lastWasFunc = false;
    private static long nextLblnum = 0;
    public APkgClassResolver currentFile;
    public boolean isSuper = false;
    public E lhsSize;
    public E lastSize;

    private final InstructionHolder<T> instructions;
    private final SizeHelper<T, E> sizeHelper;
    private final TileSet<T, E> tiles;

    public static long getNewLblNum(){
        return nextLblnum++;
    }

    public CodeGenVisitor(final Platform<T, E> platform) {
        this(null, platform);
    }

    public CodeGenVisitor(final APkgClassResolver resolver, final Platform<T, E> platform) {
        this.platform = platform;
        this.currentFile = resolver;
        this.instructions = this.platform.getInstructionHolder();
        this.sizeHelper = this.platform.getSizeHelper();
        this.tiles = this.platform.getTiles();
        this.lastSize = sizeHelper.getDefaultSize();
        codeGens.put(platform, this);
    }

    public void genHeader(final APkgClassResolver resolver) {
        this.currentFile = resolver;

        platform.genHeaderStart(instructions);

        if (!resolver.fullName.equals(JoosNonTerminal.OBJECT)){
            APkgClassResolver superResolver = null;
            superResolver = resolver.getSuper();
            platform.genInstructorInvoke(superResolver, instructions);
        }

        platform.genHeaderEnd(resolver, instructions);
        codeGens.put(platform, this);
    }

    public void genLayoutForStaticFields(final Iterable<DclSymbol> staticFields) {
        platform.genLayoutForStaticFields(staticFields, instructions);
    }

    public void visit(final FieldAccessSymbol field) {
        //generate tiles for children
        final boolean wasGetVal = getVal;
        final boolean wasSuper = isSuper;
        getVal = true;
        field.children.get(0).accept(this);
        //super.x().y() x should be from super but not y
        getVal = wasGetVal;
        if(wasSuper) isSuper = false;
        field.children.get(1).accept(this);
        isSuper = false;

        //get the best tile for field
        tiles.<FieldAccessSymbol>addBest(tiles.fieldAccess, field, platform);
    }

    public void visit(final MethodSymbol method){
        final String methodName = APkgClassResolver.generateFullId(method);
        try{
            if(APkgClassResolver.generateUniqueName(method, method.dclName).equals(JoosNonTerminal.ENTRY)
                    && method.isStatic() && !hasEntry){
                hasEntry = true;
                platform.genStartInstructions(methodName, instructions);
            }
        }catch(final Exception e){
            //Should never get here.
            e.printStackTrace();
        }

        for(final ISymbol child : method.children) child.accept(this);

        tiles.<MethodSymbol>addBest(tiles.methods, method, platform);
        platform.getBest(method).addToHolder(instructions);
    }

    public void visit(final ConstructorSymbol constructor) {
        lastWasFunc = true;
        for(final ISymbol child : constructor.children) child.accept(this);

        tiles.<ConstructorSymbol>addBest(tiles.constructors, constructor, platform);
        platform.getBest(constructor).addToHolder(instructions);
    }



    public void visit(final CreationExpression creationExpression) {

        final List<String> types = new LinkedList<String>();

        for(final ISymbol child : creationExpression.children){
            child.accept(this);
            final Typeable typeable = (Typeable) child;
            final TypeSymbol ts = typeable.getType();
            types.add(ts.getTypeDclNode().fullName);
        }

        tiles.<CreationExpression>addBest(tiles.creation, creationExpression, platform);

        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(final ANonTerminal aNonTerminal) {
        final boolean lastFunc = lastWasFunc;
        lastWasFunc = false;
        for(final ISymbol child : aNonTerminal.children) child.accept(this);
        lastWasFunc = lastFunc;
        tiles.<ANonTerminal>addBest(tiles.anonTerms, aNonTerminal, platform);
    }

    public void visit(final WhileExprSymbol whileExprSymbol) {
        whileExprSymbol.getConditionSymbol().accept(this);
        whileExprSymbol.getBody().accept(this);
        tiles.<WhileExprSymbol>addBest(tiles.whiles, whileExprSymbol, platform);
    }

    public void visit(final ForExprSymbol forExprSymbol) {
        forExprSymbol.getForInit().accept(this);
        forExprSymbol.getConditionExpr().accept(this);
        forExprSymbol.getBody().accept(this);
        forExprSymbol.getForUpdate().accept(this);
        tiles.<ForExprSymbol>addBest(tiles.fors, forExprSymbol, platform);
    }

    public void visit(final IfExprSymbol ifExprSymbol) {
        ifExprSymbol.getConditionSymbol().accept(this);
        ifExprSymbol.getifBody().accept(this);
        final ISymbol elseSymbol = ifExprSymbol.getElseBody();
        if(elseSymbol != null) elseSymbol.accept(this);
        tiles.<IfExprSymbol>addBest(tiles.ifs, ifExprSymbol, platform);
    }

    public void visit(final ReturnExprSymbol retSymbol) {
        if(retSymbol.children.size() == 1) retSymbol.children.get(0).accept(this);
        tiles.<ReturnExprSymbol>addBest(tiles.rets, retSymbol, platform);
    }

    public void visit(final CastExpressionSymbol symbol) {
        symbol.getOperandExpression().accept(this);
        tiles.<CastExpressionSymbol>addBest(tiles.casts, symbol, platform);
        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(final NegOpExprSymbol op) {
        op.children.get(0).accept(this);
        tiles.<NegOpExprSymbol>addBest(tiles.negs, op, platform);
    }

    public void visit(final NotOpExprSymbol op) {
        op.children.get(0).accept(this);
        tiles.<NotOpExprSymbol>addBest(tiles.nots, op, platform);
    }

    public void visit(final MultiplyExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);

        tiles.<MultiplyExprSymbol>addBest(tiles.mults, op, platform);
    }

    public void visit(final AssignmentExprSymbol op) {
        final ISymbol leftHandSide = op.children.get(0);
        final ISymbol rightHandSide = op.children.get(1);

        final boolean tmp = getVal;
        getVal = false;
        leftHandSide.accept(this);
        lhsSize = lastSize;
        getVal = tmp;
        rightHandSide.accept(this);
        tiles.<AssignmentExprSymbol>addBest(tiles.assigns, op, platform);
    }

    public void visit(final DivideExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);

        tiles.<DivideExprSymbol>addBest(tiles.divs, op, platform);
    }

    public void visit(final RemainderExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);

        tiles.<RemainderExprSymbol>addBest(tiles.rems, op, platform);
    }

    public void visit(final AddExprSymbol op) {
        binOpHelper(op);
        tiles.<AddExprSymbol>addBest(tiles.adds, op, platform);
    }

    public void visit(final SubtractExprSymbol op) {
        binOpHelper(op);
        tiles.<SubtractExprSymbol>addBest(tiles.subs, op, platform);
    }

    public void visit(final LSExprSymbol op) {
        binOpHelper(op);
        tiles.<LSExprSymbol>addBest(tiles.lss, op, platform);
    }

    public void visit(final RSExprSymbol op) {
        binOpHelper(op);
        tiles.<RSExprSymbol>addBest(tiles.rss, op, platform);
    }

    public void visit(final URSExprSymbol op) {
        binOpHelper(op);
        tiles.<URSExprSymbol>addBest(tiles.urss, op, platform);
    }

    public void visit(final LtExprSymbol op) {
        binOpHelper(op);
        tiles.<LtExprSymbol>addBest(tiles.lts, op, platform);
    }

    public void visit(final EqExprSymbol op) {
        binOpHelper(op);
        tiles.<EqExprSymbol>addBest(tiles.eqs, op, platform);
    }

    public void visit(final NeExprSymbol op) {
        binOpHelper(op);
        tiles.<NeExprSymbol>addBest(tiles.nes, op, platform);
    }

    public void visit(final AndExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);
        tiles.<AndExprSymbol>addBest(tiles.ands, op, platform);
    }

    public void visit(final OrExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);
        tiles.<OrExprSymbol>addBest(tiles.ors, op, platform);
    }

    public void visit(final EAndExprSymbol op) {
        binOpHelper(op);
        tiles.<EAndExprSymbol>addBest(tiles.eands, op, platform);
    }

    public void visit(final EOrExprSymbol op) {
        binOpHelper(op);
        tiles.<EOrExprSymbol>addBest(tiles.eors, op, platform);
    }

    public void visit(final LeExprSymbol op) {
        binOpHelper(op);
        tiles.<LeExprSymbol>addBest(tiles.les, op, platform);
    }

    public void visit(final InstanceOfExprSymbol op) {
        op.getLeftOperand().accept(this);
        tiles.<InstanceOfExprSymbol>addBest(tiles.insts, op, platform);
    }

    public void visit(final IntegerLiteralSymbol intLiteral) {
        tiles.<INumericLiteral>addBest(tiles.numbs, intLiteral, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void visit(final LongLiteralSymbol longLiteral) {
        tiles.<INumericLiteral>addBest(tiles.numbs, longLiteral, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void visit(final NullSymbol nullSymbol) {
        tiles.<NullSymbol>addBest(tiles.nulls, nullSymbol, platform);
        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(final BooleanLiteralSymbol boolSymbol) {
        tiles.<BooleanLiteralSymbol>addBest(tiles.bools, boolSymbol, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(1));
    }

    public void visit(final ThisSymbol thisSymbol) {
        tiles.<Thisable>addBest(tiles.thisables, thisSymbol, platform);
    }

    public void visit(final SuperSymbol superSymbol) {
        tiles.<Thisable>addBest(tiles.thisables, superSymbol, platform);
        isSuper = true;
    }

    public void visit(final StringLiteralSymbol stringSymbol) {
        tiles.<StringLiteralSymbol>addBest(tiles.strs, stringSymbol, platform);
        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(final CharacterLiteralSymbol characterSymbol) {
        tiles.<INumericLiteral>addBest(tiles.numbs, characterSymbol, platform);
        sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void visit(final ArrayAccessExprSymbol arrayAccess) {
        final boolean gettingValue = getVal;
        //always want the value of the array when accessing it's member.
        getVal = true;
        arrayAccess.children.get(0).accept(this);
        arrayAccess.children.get(1).accept(this);

        if(gettingValue){

            E elementSize;
            if(!sizeHelper.hasSetSize(arrayAccess.getType().value)){
                elementSize = sizeHelper.getDefaultSize();
            }else{
                final long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
                elementSize = sizeHelper.getSize(stackSize);
            }
            lastSize = elementSize;
            tiles.<ArrayAccessExprSymbol>addBest(tiles.arrayValues, arrayAccess, platform);
        }else{
            tiles.<ArrayAccessExprSymbol>addBest(tiles.arrayRefs, arrayAccess, platform);
            lastSize = sizeHelper.getSize(sizeHelper.getDefaultStackSize());
        }
    }

    public void visit(final DclSymbol dclSymbol) {
        if(dclSymbol.children.isEmpty()) new IntegerLiteralSymbol(0).accept(this);
        else dclSymbol.children.get(0).accept(this);
        lastSize = sizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper));
        tiles.<DclSymbol>addBest(tiles.dcls, dclSymbol, platform);
    }

    public void visit(final ByteLiteralSymbol byteLiteral) {
        tiles.<INumericLiteral>addBest(tiles.numbs, byteLiteral, platform);
        lastSize = sizeHelper.getSize(2);
    }

    public void visit(final ShortLiteralSymbol shortLiteral) {
        tiles.<INumericLiteral>addBest(tiles.numbs, shortLiteral, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void printToFileAndEmpty(final PrintStream printer){
        instructions.flush(printer);
    }

    private void binOpHelper(final BinOpExpr bin){
        bin.children.get(0).accept(this);
        bin.children.get(1).accept(this);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(bin.getType().getTypeDclNode().fullName));
    }

    public void visit(final SimpleMethodInvoke invoke) {
        for(final ISymbol arg : invoke.children) arg.accept(this);
        tiles.<SimpleMethodInvoke>addBest(tiles.invokes, invoke, platform);
    }

    public void visit(final SimpleNameSymbol name) {
        final DclSymbol dcl = name.dcl;

        lastSize = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRefStackSize(sizeHelper));

        if(getVal){
            tiles.<SimpleNameSymbol>addBest(tiles.nameValues, name, platform);
        }else{
            tiles.<SimpleNameSymbol>addBest(tiles.nameRefs, name, platform);
        }
    }

    public void visit(final EmptyStatementSymbol empty){
        tiles.addEmpty(empty, platform);
    }

    public void visit(final ISymbol other){
        throw new IllegalArgumentException(other.toString());
    }
}