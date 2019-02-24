package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.expressions.*;
import cs444.types.APkgClassResolver;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeGenVisitor<T extends Instruction<T>, E extends Enum<E>> {
    public static final String INIT_OBJECT_FUNC = "__init_object";
    public static final String NATIVE_NAME = "NATIVE";
    private static Map<Platform<?, ?>, CodeGenVisitor<?, ?>> codeGens = new HashMap<Platform<?, ?>, CodeGenVisitor<?,
            ?>>();
    private static long nextLblnum = 0;
    public final Platform<T, E> platform;
    private final InstructionHolder<T> instructions;
    private final SizeHelper<T, E> sizeHelper;
    private final TileSet<T, E> tiles;
    public boolean getVal = true;
    public boolean lastWasFunc = false;
    public APkgClassResolver currentFile;
    public boolean isSuper = false;
    public E lhsSize;
    public E lastSize;
    private boolean hasEntry = false;

    public CodeGenVisitor(Platform<T, E> platform) {
        this(null, platform);
    }

    public CodeGenVisitor(APkgClassResolver resolver, Platform<T, E> platform) {
        this.platform = platform;
        currentFile = resolver;
        instructions = this.platform.getInstructionHolder();
        sizeHelper = this.platform.getSizeHelper();
        tiles = this.platform.getTiles();
        lastSize = sizeHelper.getDefaultSize();
        codeGens.put(platform, this);
    }

    //NOTE: this is for testing
    public static void reset() {
        codeGens = new HashMap<Platform<?, ?>, CodeGenVisitor<?, ?>>();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> CodeGenVisitor<T, E> getCurrentCodeGen(Platform<T, E> platform) {

        return (CodeGenVisitor<T, E>) codeGens.get(platform);
    }

    public static long getNewLblNum() {
        return nextLblnum++;
    }

    public void genHeader(APkgClassResolver resolver) {
        currentFile = resolver;

        platform.genHeaderStart(instructions);

        if (!resolver.fullName.equals(JoosNonTerminal.OBJECT)) {
            APkgClassResolver superResolver = null;
            superResolver = resolver.getSuper();
            platform.genInstructorInvoke(superResolver, instructions);
        }

        platform.genHeaderEnd(resolver, instructions);

        codeGens.put(platform, this);
    }

    public void genLayoutForStaticFields(Iterable<DclSymbol> staticFields) {
        platform.genLayoutForStaticFields(staticFields, instructions);
    }

    public void visit(FieldAccessSymbol field) {
        //generate tiles for children
        boolean wasGetVal = getVal;
        boolean wasSuper = isSuper;
        getVal = true;
        field.children.get(0).accept(this);
        //super.x().y() x should be from super but not y
        getVal = wasGetVal;
        if (wasSuper) {
            isSuper = false;
        }
        field.children.get(1).accept(this);
        isSuper = false;

        //get the best tile for field
        tiles.addBest(tiles.fieldAccess, field, platform);
    }

    public void visit(MethodSymbol method) {
        String methodName = APkgClassResolver.generateFullId(method);
        try {
            if (APkgClassResolver.generateUniqueName(method, method.dclName).equals(JoosNonTerminal.ENTRY) && method.isStatic()
                    && !hasEntry) {
                hasEntry = true;
                platform.genStartInstructions(methodName, instructions);
            }
        } catch (Exception e) {
            //Should never get here.
            e.printStackTrace();
        }

        for (ISymbol child : method.children) {
            child.accept(this);
        }

        tiles.addBest(tiles.methods, method, platform);
        platform.getBest(method).addToHolder(instructions);
    }

    public void visit(ConstructorSymbol constructor) {
        lastWasFunc = true;
        for (ISymbol child : constructor.children) {
            child.accept(this);
        }

        tiles.addBest(tiles.constructors, constructor, platform);
        platform.getBest(constructor).addToHolder(instructions);
    }

    public void visit(CreationExpression creationExpression) {

        List<String> types = new LinkedList<String>();

        for (ISymbol child : creationExpression.children) {
            child.accept(this);
            Typeable typeable = (Typeable) child;
            TypeSymbol ts = typeable.getType();
            types.add(ts.getTypeDclNode().fullName);
        }

        tiles.addBest(tiles.creation, creationExpression, platform);

        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(ANonTerminal aNonTerminal) {
        boolean lastFunc = lastWasFunc;
        lastWasFunc = false;
        for (ISymbol child : aNonTerminal.children) {
            child.accept(this);
        }
        lastWasFunc = lastFunc;
        tiles.addBest(tiles.anonTerms, aNonTerminal, platform);
    }

    public void visit(WhileExprSymbol whileExprSymbol) {
        whileExprSymbol.getConditionSymbol().accept(this);
        whileExprSymbol.getBody().accept(this);
        tiles.addBest(tiles.whiles, whileExprSymbol, platform);
    }

    public void visit(ForExprSymbol forExprSymbol) {
        forExprSymbol.getForInit().accept(this);
        forExprSymbol.getConditionExpr().accept(this);
        forExprSymbol.getBody().accept(this);
        forExprSymbol.getForUpdate().accept(this);
        tiles.addBest(tiles.fors, forExprSymbol, platform);
    }

    public void visit(IfExprSymbol ifExprSymbol) {
        ifExprSymbol.getConditionSymbol().accept(this);
        ifExprSymbol.getifBody().accept(this);
        ISymbol elseSymbol = ifExprSymbol.getElseBody();
        if (elseSymbol != null) {
            elseSymbol.accept(this);
        }
        tiles.addBest(tiles.ifs, ifExprSymbol, platform);
    }

    public void visit(ReturnExprSymbol retSymbol) {
        if (retSymbol.children.size() == 1) {
            retSymbol.children.get(0).accept(this);
        }
        tiles.addBest(tiles.rets, retSymbol, platform);
    }

    public void visit(CastExpressionSymbol symbol) {
        symbol.getOperandExpression().accept(this);
        tiles.addBest(tiles.casts, symbol, platform);
        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(NegOpExprSymbol op) {
        op.children.get(0).accept(this);
        tiles.addBest(tiles.negs, op, platform);
    }

    public void visit(NotOpExprSymbol op) {
        op.children.get(0).accept(this);
        tiles.addBest(tiles.nots, op, platform);
    }

    public void visit(MultiplyExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);

        tiles.addBest(tiles.mults, op, platform);
    }

    public void visit(AssignmentExprSymbol op) {
        ISymbol leftHandSide = op.children.get(0);
        ISymbol rightHandSide = op.children.get(1);

        boolean tmp = getVal;
        getVal = false;
        leftHandSide.accept(this);
        lhsSize = lastSize;
        getVal = tmp;
        rightHandSide.accept(this);
        tiles.addBest(tiles.assigns, op, platform);
    }

    public void visit(DivideExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);

        tiles.addBest(tiles.divs, op, platform);
    }

    public void visit(RemainderExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);

        tiles.addBest(tiles.rems, op, platform);
    }

    public void visit(AddExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.adds, op, platform);
    }

    public void visit(SubtractExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.subs, op, platform);
    }

    public void visit(LSExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.lss, op, platform);
    }

    public void visit(RSExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.rss, op, platform);
    }

    public void visit(URSExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.urss, op, platform);
    }

    public void visit(LtExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.lts, op, platform);
    }

    public void visit(GtExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.gts, op, platform);
    }

    public void visit(EqExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.eqs, op, platform);
    }

    public void visit(NeExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.nes, op, platform);
    }

    public void visit(AndExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);
        tiles.addBest(tiles.ands, op, platform);
    }

    public void visit(OrExprSymbol op) {
        op.children.get(0).accept(this);
        op.children.get(1).accept(this);
        tiles.addBest(tiles.ors, op, platform);
    }

    public void visit(EAndExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.eands, op, platform);
    }

    public void visit(EOrExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.eors, op, platform);
    }

    public void visit(LeExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.les, op, platform);
    }

    public void visit(GeExprSymbol op) {
        binOpHelper(op);
        tiles.addBest(tiles.ges, op, platform);
    }

    public void visit(InstanceOfExprSymbol op) {
        op.getLeftOperand().accept(this);
        tiles.addBest(tiles.insts, op, platform);
    }

    public void visit(IntegerLiteralSymbol intLiteral) {
        tiles.addBest(tiles.numbs, intLiteral, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void visit(LongLiteralSymbol longLiteral) {
        tiles.addBest(tiles.numbs, longLiteral, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void visit(NullSymbol nullSymbol) {
        tiles.addBest(tiles.nulls, nullSymbol, platform);
        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(BooleanLiteralSymbol boolSymbol) {
        tiles.addBest(tiles.bools, boolSymbol, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(1));
    }

    public void visit(ThisSymbol thisSymbol) {
        tiles.addBest(tiles.thisables, thisSymbol, platform);
    }

    public void visit(SuperSymbol superSymbol) {
        tiles.addBest(tiles.thisables, superSymbol, platform);
        isSuper = true;
    }

    public void visit(StringLiteralSymbol stringSymbol) {
        tiles.addBest(tiles.strs, stringSymbol, platform);
        lastSize = sizeHelper.getDefaultSize();
    }

    public void visit(CharacterLiteralSymbol characterSymbol) {
        tiles.addBest(tiles.numbs, characterSymbol, platform);
        sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void visit(ArrayAccessExprSymbol arrayAccess) {
        boolean gettingValue = getVal;
        //always want the value of the array when accessing it's member.
        getVal = true;
        arrayAccess.children.get(0).accept(this);
        arrayAccess.children.get(1).accept(this);

        if (gettingValue) {

            E elementSize;
            if (!sizeHelper.hasSetSize(arrayAccess.getType().value)) {
                elementSize = sizeHelper.getDefaultSize();
            } else {
                long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
                elementSize = sizeHelper.getSize(stackSize);
            }
            lastSize = elementSize;
            tiles.addBest(tiles.arrayValues, arrayAccess, platform);
        } else {
            tiles.addBest(tiles.arrayRefs, arrayAccess, platform);
            lastSize = sizeHelper.getSize(sizeHelper.getDefaultStackSize());
        }
    }

    public void visit(DclSymbol dclSymbol) {
        if (dclSymbol.children.isEmpty()) {
            new IntegerLiteralSymbol(0).accept(this);
        } else {
            dclSymbol.children.get(0).accept(this);
        }
        lastSize = sizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper));
        tiles.addBest(tiles.dcls, dclSymbol, platform);
    }

    public void visit(ByteLiteralSymbol byteLiteral) {
        tiles.addBest(tiles.numbs, byteLiteral, platform);
        lastSize = sizeHelper.getSize(2);
    }

    public void visit(ShortLiteralSymbol shortLiteral) {
        tiles.addBest(tiles.numbs, shortLiteral, platform);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSize(2));
    }

    public void printToFileAndEmpty(PrintStream printer) {
        instructions.flush(platform, printer);
    }

    private void binOpHelper(BinOpExpr bin) {
        bin.children.get(0).accept(this);
        bin.children.get(1).accept(this);
        lastSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(bin.getType().getTypeDclNode().fullName));
    }

    public void visit(SimpleMethodInvoke invoke) {
        for (ISymbol arg : invoke.children) {
            arg.accept(this);
        }
        tiles.addBest(tiles.invokes, invoke, platform);
    }

    public void visit(SimpleNameSymbol name) {
        DclSymbol dcl = name.dcl;

        lastSize = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRefStackSize(sizeHelper));

        if (getVal) {
            tiles.addBest(tiles.nameValues, name, platform);
        } else {
            tiles.addBest(tiles.nameRefs, name, platform);
        }
    }

    public void visit(EmptyStatementSymbol empty) {
        tiles.addEmpty(empty, platform);
    }

    public void visit(ISymbol other) {
        throw new IllegalArgumentException(other.toString());
    }
}