package cs444.codegen.tiles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.codegen.IPlatform;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;

public class TileSet <T extends Instruction>{
    private static final Map<Class<? extends Instruction>, TileSet<? extends Instruction>> classMap =
            new HashMap<Class<? extends Instruction>, TileSet<? extends Instruction>>();

    public final List<ITile<T, SimpleNameSymbol>> nameValues = new LinkedList<ITile<T, SimpleNameSymbol>>();

    public static <T extends Instruction> TileSet<T> getOrMake(final Class<T> klass){
        @SuppressWarnings("unchecked")
        TileSet<T> tileSet = (TileSet<T>)classMap.get(klass);

        if(tileSet == null){
            tileSet = new TileSet<T>();
        }

        return tileSet;
    }

    public final <S extends ISymbol> void addBest(final List<ITile<T, S>> tiles, final S symbol, final IPlatform<T> platform){

        InstructionsAndTiming<T> best = null;

        for(final ITile<T, S> tile : tiles){
            if(tile.fits(symbol)){
                final InstructionsAndTiming<T> on = tile.generate(symbol, platform);
                if(on.isBetterThan(best)) best = on;
            }
        }

        best.addToHolder(platform.getInstructionHolder());
    }

    public void getValue(final SimpleNameSymbol name, final IPlatform<T> platform){
       this.<SimpleNameSymbol>addBest(nameValues, name, platform);
    }
    public void getRef(final SimpleNameSymbol name, final IPlatform<T> platform){
        //TODO
    }
}
