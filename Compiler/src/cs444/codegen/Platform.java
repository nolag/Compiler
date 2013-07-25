package cs444.codegen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class Platform<T extends Instruction, E extends Enum<E>> {
    private final Map<ISymbol, InstructionsAndTiming<T>> bests = new HashMap<ISymbol, InstructionsAndTiming<T>> ();

    public void addBest(final ISymbol symbol, final InstructionsAndTiming<T> tile){
        bests.put(symbol, tile);
    }

    public InstructionsAndTiming<T> getBest(final ISymbol symbol){
        return bests.get(symbol);
    }

    public CodeGenVisitor<T, E> makeNewCodeGen(){
        return new CodeGenVisitor<T, E>(this);
    }

    public abstract SizeHelper<T, E> getSizeHelper();
    public abstract ObjectLayout<T> getObjectLayout();
    public abstract SelectorIndexedTable<T> getSelectorIndex();
    public abstract void makeSubtypeTable(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException;

    public abstract SubtypeIndexedTable<T> getSubtypeTable();
    public abstract IRuntime<T> getRunime();
    public abstract InstructionHolder<T> getInstructionHolder();
    public abstract void generateStaticCode(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException;

    //Functions for file headers
    public abstract void genStartInstructions(final String methodName, final Addable<T> instructions);
    public abstract void genInstructorInvoke(final APkgClassResolver resolver, final Addable<T> instructions);
    public abstract void genHeaderStart(final Addable<T> instructions);
    public abstract void genHeaderEnd(final APkgClassResolver resolver, final Addable<T> instructions);

    public abstract TileSet<T, E> getTiles();

    public abstract void zeroDefaultLocation(final Addable<T> instructions);

    public abstract void genLayoutForStaticFields(final Iterable<DclSymbol> staticFields, final Addable<T> instructions);

    public abstract TileHelper<T, E> getTileHelper();
}
