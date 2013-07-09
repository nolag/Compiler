package cs444.codegen.tiles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.*;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.parser.symbols.ast.expressions.*;

public class TileSet <T extends Instruction, U extends SizeHelper<T, ?>>{
    private static final Map<Class<? extends Instruction>, TileSet<? extends Instruction, ? extends SizeHelper<?, ?>>> classMap =
            new HashMap<Class<? extends Instruction>, TileSet<? extends Instruction, ? extends SizeHelper<?, ?>>>();

    public final List<ITile<T, U, SimpleNameSymbol>> nameValues = new LinkedList<ITile<T, U, SimpleNameSymbol>>();
    public final List<ITile<T, U, SimpleNameSymbol>> nameRefs = new LinkedList<ITile<T, U, SimpleNameSymbol>>();
    public final List<ITile<T, U, FieldAccessSymbol>> fieldAccess = new LinkedList<ITile<T, U, FieldAccessSymbol>>();
    public final List<ITile<T, U, MethodSymbol>> methods = new LinkedList<ITile<T, U, MethodSymbol>>();
    public final List<ITile<T, U,  ConstructorSymbol>> constructors = new LinkedList<ITile<T, U,  ConstructorSymbol>>();
    public final List<ITile<T, U,  CreationExpression>> creation = new LinkedList<ITile<T, U,  CreationExpression>>();
    public final List<ITile<T, U,  ANonTerminal>> anonTerms = new LinkedList<ITile<T, U,  ANonTerminal>>();
    public final List<ITile<T, U,  WhileExprSymbol>> whiles = new LinkedList<ITile<T, U,  WhileExprSymbol>>();
    public final List<ITile<T, U,  ForExprSymbol>> fors = new LinkedList<ITile<T, U,  ForExprSymbol>>();
    public final List<ITile<T, U,  IfExprSymbol>> ifs = new LinkedList<ITile<T, U,  IfExprSymbol>>();
    public final List<ITile<T, U,  ReturnExprSymbol>> rets = new LinkedList<ITile<T, U,  ReturnExprSymbol>>();
    public final List<ITile<T, U,  CastExpressionSymbol>> casts = new LinkedList<ITile<T, U,  CastExpressionSymbol>>();
    public final List<ITile<T, U,  NegOpExprSymbol>> negs = new LinkedList<ITile<T, U,  NegOpExprSymbol>>();
    public final List<ITile<T, U,  NotOpExprSymbol>> nots = new LinkedList<ITile<T, U,  NotOpExprSymbol>>();
    public final List<ITile<T, U,  MultiplyExprSymbol>> mults = new LinkedList<ITile<T, U,  MultiplyExprSymbol>>();
    public final List<ITile<T, U,  AssignmentExprSymbol>> assigns = new LinkedList<ITile<T, U,  AssignmentExprSymbol>>();
    public final List<ITile<T, U,  DivideExprSymbol>> divs = new LinkedList<ITile<T, U,  DivideExprSymbol>>();
    public final List<ITile<T, U,  RemainderExprSymbol>> rems = new LinkedList<ITile<T, U,  RemainderExprSymbol>>();
    public final List<ITile<T, U,  AddExprSymbol>> adds = new LinkedList<ITile<T, U,  AddExprSymbol>>();
    public final List<ITile<T, U,  SubtractExprSymbol>> subs = new LinkedList<ITile<T, U,  SubtractExprSymbol>>();
    public final List<ITile<T, U,  LSExprSymbol>> lss = new LinkedList<ITile<T, U,  LSExprSymbol>>();
    public final List<ITile<T, U,  RSExprSymbol>> rss = new LinkedList<ITile<T, U,  RSExprSymbol>>();
    public final List<ITile<T, U,  URSExprSymbol>> urss = new LinkedList<ITile<T, U,  URSExprSymbol>>();
    public final List<ITile<T, U,  LtExprSymbol>> lts = new LinkedList<ITile<T, U,  LtExprSymbol>>();
    public final List<ITile<T, U,  LeExprSymbol>> les = new LinkedList<ITile<T, U,  LeExprSymbol>>();
    public final List<ITile<T, U,  EqExprSymbol>> eqs = new LinkedList<ITile<T, U,  EqExprSymbol>>();
    public final List<ITile<T, U,  NeExprSymbol>> nes = new LinkedList<ITile<T, U,  NeExprSymbol>>();
    public final List<ITile<T, U,  EAndExprSymbol>> eands = new LinkedList<ITile<T, U,  EAndExprSymbol>>();
    public final List<ITile<T, U,  EOrExprSymbol>> eors = new LinkedList<ITile<T, U,  EOrExprSymbol>>();
    public final List<ITile<T, U,  AndExprSymbol>> ands = new LinkedList<ITile<T, U,  AndExprSymbol>>();
    public final List<ITile<T, U,  OrExprSymbol>> ors = new LinkedList<ITile<T, U,  OrExprSymbol>>();
    public final List<ITile<T, U,  InstanceOfExprSymbol>> insts = new LinkedList<ITile<T, U,  InstanceOfExprSymbol>>();
    public final List<ITile<T, U,  INumericLiteral>> numbs = new LinkedList<ITile<T, U,  INumericLiteral>>();
    public final List<ITile<T, U,  NullSymbol>> nulls = new LinkedList<ITile<T, U,  NullSymbol>>();
    public final List<ITile<T, U,  BooleanLiteralSymbol>> bools = new LinkedList<ITile<T, U,  BooleanLiteralSymbol>>();
    public final List<ITile<T, U,  Thisable>> thisables = new LinkedList<ITile<T, U,  Thisable>>();
    public final List<ITile<T, U,  StringLiteralSymbol>> strs = new LinkedList<ITile<T, U,  StringLiteralSymbol>>();
    public final List<ITile<T, U,  ArrayAccessExprSymbol>> arrayValues = new LinkedList<ITile<T, U,  ArrayAccessExprSymbol>>();
    public final List<ITile<T, U,  ArrayAccessExprSymbol>> arrayRefs = new LinkedList<ITile<T, U,  ArrayAccessExprSymbol>>();
    public final List<ITile<T, U,  DclSymbol>> dcls = new LinkedList<ITile<T, U,  DclSymbol>>();
    public final List<ITile<T, U,  SimpleMethodInvoke>> invokes = new LinkedList<ITile<T, U,  SimpleMethodInvoke>>();

    public static <T extends Instruction, U extends SizeHelper<T, ?>> TileSet<T, U> getOrMake(final Class<T> klass){
        @SuppressWarnings("unchecked")
        TileSet<T, U> tileSet = (TileSet<T, U>)classMap.get(klass);

        if(tileSet == null){
            tileSet = new TileSet<T, U>();
            classMap.put(klass, tileSet);
        }

        return tileSet;
    }

    public final <S extends ISymbol> void addBest(final List<ITile<T, U, S>> tiles, final S symbol, final Platform<T, U> platform){

        InstructionsAndTiming<T> best = null;

        for(final ITile<T, U, S> tile : tiles){
            if(tile.fits(symbol)){
                final InstructionsAndTiming<T> on = tile.generate(symbol, platform);
                if(on.isBetterThan(best)) best = on;
            }
        }

        platform.addBest(symbol, best);
    }

    public final <S extends ISymbol> void addEmpty(final S symbol, final Platform<T, ?> platform){
        platform.addBest(symbol, new InstructionsAndTiming<T>());
    }
}