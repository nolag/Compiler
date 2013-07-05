package cs444.codegen.tiles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.expressions.*;

public class TileSet <T extends Instruction>{
    private static final Map<Class<? extends Instruction>, TileSet<? extends Instruction>> classMap =
            new HashMap<Class<? extends Instruction>, TileSet<? extends Instruction>>();

    public final List<ITile<T, SimpleNameSymbol>> nameValues = new LinkedList<ITile<T, SimpleNameSymbol>>();
    public final List<ITile<T, SimpleNameSymbol>> nameRefs = new LinkedList<ITile<T, SimpleNameSymbol>>();
    public final List<ITile<T, FieldAccessSymbol>> fieldAccess = new LinkedList<ITile<T, FieldAccessSymbol>>();
    public final List<ITile<T, MethodSymbol>> methods = new LinkedList<ITile<T, MethodSymbol>>();
    public final List<ITile<T, ConstructorSymbol>> constructors = new LinkedList<ITile<T, ConstructorSymbol>>();
    public final List<ITile<T, CreationExpression>> creation = new LinkedList<ITile<T, CreationExpression>>();
    public final List<ITile<T, ANonTerminal>> anonTerms = new LinkedList<ITile<T, ANonTerminal>>();
    public final List<ITile<T, WhileExprSymbol>> whiles = new LinkedList<ITile<T, WhileExprSymbol>>();
    public final List<ITile<T, ForExprSymbol>> fors = new LinkedList<ITile<T, ForExprSymbol>>();
    public final List<ITile<T, IfExprSymbol>> ifs = new LinkedList<ITile<T, IfExprSymbol>>();
    public final List<ITile<T, ReturnExprSymbol>> rets = new LinkedList<ITile<T, ReturnExprSymbol>>();
    public final List<ITile<T, CastExpressionSymbol>> casts = new LinkedList<ITile<T, CastExpressionSymbol>>();
    public final List<ITile<T, NegOpExprSymbol>> negs = new LinkedList<ITile<T, NegOpExprSymbol>>();
    public final List<ITile<T, NotOpExprSymbol>> nots = new LinkedList<ITile<T, NotOpExprSymbol>>();
    public final List<ITile<T, MultiplyExprSymbol>> mults = new LinkedList<ITile<T, MultiplyExprSymbol>>();
    public final List<ITile<T, AssignmentExprSymbol>> assigns = new LinkedList<ITile<T, AssignmentExprSymbol>>();
    public final List<ITile<T, DivideExprSymbol>> divs = new LinkedList<ITile<T, DivideExprSymbol>>();
    public final List<ITile<T, RemainderExprSymbol>> rems = new LinkedList<ITile<T, RemainderExprSymbol>>();
    public final List<ITile<T, AddExprSymbol>> adds = new LinkedList<ITile<T, AddExprSymbol>>();
    public final List<ITile<T, SubtractExprSymbol>> subs = new LinkedList<ITile<T, SubtractExprSymbol>>();
    public final List<ITile<T, LSExprSymbol>> lss = new LinkedList<ITile<T, LSExprSymbol>>();
    public final List<ITile<T, RSExprSymbol>> rss = new LinkedList<ITile<T, RSExprSymbol>>();
    public final List<ITile<T, URSExprSymbol>> urss = new LinkedList<ITile<T, URSExprSymbol>>();
    public final List<ITile<T, LtExprSymbol>> lts = new LinkedList<ITile<T, LtExprSymbol>>();
    public final List<ITile<T, LeExprSymbol>> les = new LinkedList<ITile<T, LeExprSymbol>>();
    public final List<ITile<T, EqExprSymbol>> eqs = new LinkedList<ITile<T, EqExprSymbol>>();
    public final List<ITile<T, NeExprSymbol>> nes = new LinkedList<ITile<T, NeExprSymbol>>();
    public final List<ITile<T, EAndExprSymbol>> eands = new LinkedList<ITile<T, EAndExprSymbol>>();
    public final List<ITile<T, EOrExprSymbol>> eors = new LinkedList<ITile<T, EOrExprSymbol>>();
    public final List<ITile<T, AndExprSymbol>> ands = new LinkedList<ITile<T, AndExprSymbol>>();
    public final List<ITile<T, OrExprSymbol>> ors = new LinkedList<ITile<T, OrExprSymbol>>();
    public final List<ITile<T, InstanceOfExprSymbol>> insts = new LinkedList<ITile<T, InstanceOfExprSymbol>>();
    public final List<ITile<T, INumericLiteral>> numbs = new LinkedList<ITile<T, INumericLiteral>>();
    public final List<ITile<T, NullSymbol>> nulls = new LinkedList<ITile<T, NullSymbol>>();
    public final List<ITile<T, BooleanLiteralSymbol>> bools = new LinkedList<ITile<T, BooleanLiteralSymbol>>();
    public final List<ITile<T, Thisable>> thisables = new LinkedList<ITile<T, Thisable>>();
    public final List<ITile<T, StringLiteralSymbol>> strs = new LinkedList<ITile<T, StringLiteralSymbol>>();
    public final List<ITile<T, ArrayAccessExprSymbol>> arrayValues = new LinkedList<ITile<T, ArrayAccessExprSymbol>>();
    public final List<ITile<T, ArrayAccessExprSymbol>> arrayRefs = new LinkedList<ITile<T, ArrayAccessExprSymbol>>();
    public final List<ITile<T, DclSymbol>> dcls = new LinkedList<ITile<T, DclSymbol>>();
    public final List<ITile<T, SimpleMethodInvoke>> invokes = new LinkedList<ITile<T, SimpleMethodInvoke>>();

    public static <T extends Instruction> TileSet<T> getOrMake(final Class<T> klass){
        @SuppressWarnings("unchecked")
        TileSet<T> tileSet = (TileSet<T>)classMap.get(klass);

        if(tileSet == null){
            tileSet = new TileSet<T>();
            classMap.put(klass, tileSet);
        }

        return tileSet;
    }

    public final <S extends ISymbol> void addBest(final List<ITile<T, S>> tiles, final S symbol, final Platform<T> platform){

        InstructionsAndTiming<T> best = null;

        for(final ITile<T, S> tile : tiles){
            if(tile.fits(symbol)){
                final InstructionsAndTiming<T> on = tile.generate(symbol, platform);
                if(on.isBetterThan(best)) best = on;
            }
        }

        platform.addBest(symbol, best);
    }

    public final <S extends ISymbol> void addEmpty(final S symbol, final Platform<T> platform){
        platform.addBest(symbol, new InstructionsAndTiming<T>());
    }
}