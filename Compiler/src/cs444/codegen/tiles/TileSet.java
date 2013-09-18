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

public class TileSet <T extends Instruction, E extends Enum<E>>{
    private static Map<Class<? extends Platform<?, ?>>, TileSet<?, ?>> classMap =new HashMap<>();

    public final List<ITile<T, E, SimpleNameSymbol>> nameValues = new LinkedList<ITile<T, E, SimpleNameSymbol>>();
    public final List<ITile<T, E, SimpleNameSymbol>> nameRefs = new LinkedList<ITile<T, E, SimpleNameSymbol>>();
    public final List<ITile<T, E, FieldAccessSymbol>> fieldAccess = new LinkedList<ITile<T, E, FieldAccessSymbol>>();
    public final List<ITile<T, E, MethodSymbol>> methods = new LinkedList<ITile<T, E, MethodSymbol>>();
    public final List<ITile<T, E,  ConstructorSymbol>> constructors = new LinkedList<ITile<T, E,  ConstructorSymbol>>();
    public final List<ITile<T, E,  CreationExpression>> creation = new LinkedList<ITile<T, E,  CreationExpression>>();
    public final List<ITile<T, E,  ANonTerminal>> anonTerms = new LinkedList<ITile<T, E,  ANonTerminal>>();
    public final List<ITile<T, E,  WhileExprSymbol>> whiles = new LinkedList<ITile<T, E,  WhileExprSymbol>>();
    public final List<ITile<T, E,  ForExprSymbol>> fors = new LinkedList<ITile<T, E,  ForExprSymbol>>();
    public final List<ITile<T, E,  IfExprSymbol>> ifs = new LinkedList<ITile<T, E,  IfExprSymbol>>();
    public final List<ITile<T, E,  ReturnExprSymbol>> rets = new LinkedList<ITile<T, E,  ReturnExprSymbol>>();
    public final List<ITile<T, E,  CastExpressionSymbol>> casts = new LinkedList<ITile<T, E,  CastExpressionSymbol>>();
    public final List<ITile<T, E,  NegOpExprSymbol>> negs = new LinkedList<ITile<T, E,  NegOpExprSymbol>>();
    public final List<ITile<T, E,  NotOpExprSymbol>> nots = new LinkedList<ITile<T, E,  NotOpExprSymbol>>();
    public final List<ITile<T, E,  MultiplyExprSymbol>> mults = new LinkedList<ITile<T, E,  MultiplyExprSymbol>>();
    public final List<ITile<T, E,  AssignmentExprSymbol>> assigns = new LinkedList<ITile<T, E,  AssignmentExprSymbol>>();
    public final List<ITile<T, E,  DivideExprSymbol>> divs = new LinkedList<ITile<T, E,  DivideExprSymbol>>();
    public final List<ITile<T, E,  RemainderExprSymbol>> rems = new LinkedList<ITile<T, E,  RemainderExprSymbol>>();
    public final List<ITile<T, E,  AddExprSymbol>> adds = new LinkedList<ITile<T, E,  AddExprSymbol>>();
    public final List<ITile<T, E,  SubtractExprSymbol>> subs = new LinkedList<ITile<T, E,  SubtractExprSymbol>>();
    public final List<ITile<T, E,  LSExprSymbol>> lss = new LinkedList<ITile<T, E,  LSExprSymbol>>();
    public final List<ITile<T, E,  RSExprSymbol>> rss = new LinkedList<ITile<T, E,  RSExprSymbol>>();
    public final List<ITile<T, E,  URSExprSymbol>> urss = new LinkedList<ITile<T, E,  URSExprSymbol>>();
    public final List<ITile<T, E,  LtExprSymbol>> lts = new LinkedList<ITile<T, E,  LtExprSymbol>>();
    public final List<ITile<T, E,  LeExprSymbol>> les = new LinkedList<ITile<T, E,  LeExprSymbol>>();
    public final List<ITile<T, E,  EqExprSymbol>> eqs = new LinkedList<ITile<T, E,  EqExprSymbol>>();
    public final List<ITile<T, E,  NeExprSymbol>> nes = new LinkedList<ITile<T, E,  NeExprSymbol>>();
    public final List<ITile<T, E,  EAndExprSymbol>> eands = new LinkedList<ITile<T, E,  EAndExprSymbol>>();
    public final List<ITile<T, E,  EOrExprSymbol>> eors = new LinkedList<ITile<T, E,  EOrExprSymbol>>();
    public final List<ITile<T, E,  AndExprSymbol>> ands = new LinkedList<ITile<T, E,  AndExprSymbol>>();
    public final List<ITile<T, E,  OrExprSymbol>> ors = new LinkedList<ITile<T, E,  OrExprSymbol>>();
    public final List<ITile<T, E,  InstanceOfExprSymbol>> insts = new LinkedList<ITile<T, E,  InstanceOfExprSymbol>>();
    public final List<ITile<T, E,  INumericLiteral>> numbs = new LinkedList<ITile<T, E,  INumericLiteral>>();
    public final List<ITile<T, E,  NullSymbol>> nulls = new LinkedList<ITile<T, E,  NullSymbol>>();
    public final List<ITile<T, E,  BooleanLiteralSymbol>> bools = new LinkedList<ITile<T, E,  BooleanLiteralSymbol>>();
    public final List<ITile<T, E,  Thisable>> thisables = new LinkedList<ITile<T, E,  Thisable>>();
    public final List<ITile<T, E,  StringLiteralSymbol>> strs = new LinkedList<ITile<T, E,  StringLiteralSymbol>>();
    public final List<ITile<T, E,  ArrayAccessExprSymbol>> arrayValues = new LinkedList<ITile<T, E,  ArrayAccessExprSymbol>>();
    public final List<ITile<T, E,  ArrayAccessExprSymbol>> arrayRefs = new LinkedList<ITile<T, E,  ArrayAccessExprSymbol>>();
    public final List<ITile<T, E,  DclSymbol>> dcls = new LinkedList<ITile<T, E,  DclSymbol>>();
    public final List<ITile<T, E,  SimpleMethodInvoke>> invokes = new LinkedList<ITile<T, E,  SimpleMethodInvoke>>();

    //NOTE this function is for testing
    public static void reset(){
        classMap = new HashMap<>();
    }

    public static <T extends Instruction, E extends Enum<E>> TileSet<T, E> getOrMake(final Class<? extends Platform<T, E>> klass) {
        @SuppressWarnings("unchecked")
        TileSet<T, E> tileSet = (TileSet<T, E>)classMap.get(klass);

        if(tileSet == null){
            tileSet = new TileSet<T, E>();
            classMap.put(klass, tileSet);
        }

        return tileSet;
    }

    public final <S extends ISymbol> void addBest(final List<ITile<T, E, S>> tiles,
            final S symbol, final Platform<T, E> platform){

        InstructionsAndTiming<T> best = null;

        for(final ITile<T, E, S> tile : tiles){
            if(tile.fits(symbol, platform)){
                final InstructionsAndTiming<T> on = tile.generate(symbol, platform);
                if(on.isBetterThan(best)) best = on;
            }
        }

        platform.addBest(symbol, best);
    }

    public final <S extends ISymbol> void addEmpty(final S symbol, final Platform<T, E> platform){
        platform.addBest(symbol, new InstructionsAndTiming<T>());
    }
}