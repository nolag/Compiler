package cs444.codegen;

import java.io.IOException;
import java.util.*;

import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class Platform<T extends Instruction, E extends Enum<E>> {
    public static final String NO_PEEPHOLE = "--no_peep";

    public interface PlatformFactory<T extends Instruction, E extends Enum<E>, P extends Platform<T, E>>{
        P getPlatform(Set<String> opts);
    }

    private final Set<String> options;

    protected Platform(final Set<String> options){
        this.options = new HashSet<>(options);
    }

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

    public void generateSIT(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException {
        getSelectorIndex().generateSIT(this, resolvers, outputFile, directory);
    }

    @Override
    public boolean equals(final Object other) {
        if(other == null) return false;
        if(getClass() != other.getClass()) return false;
        @SuppressWarnings("unchecked")
        final Platform<T, E> platform = (Platform<T, E>) other;
        return options.equals(platform.options);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ options.hashCode();
    }

    public abstract SizeHelper<T, E> getSizeHelper();
    public abstract ObjectLayout<T> getObjectLayout();
    public abstract SelectorIndexedTable<T, E> getSelectorIndex();
    public abstract void makeSubtypeTable(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException;

    public abstract SubtypeIndexedTable<T, E> getSubtypeTable();
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

    public abstract void moveStaticLong(final String staticLbl, final Addable<T> instructions);

    public abstract void zeroStaticLong(final String staticLbl, final Addable<T> instructions);

    public abstract String getOutputDir();

    public abstract T [] getAssemblerDirectives();

    //These functions are used for testing and can be changed to assemble or execute differently if cross compiling

    public abstract String [] getLinkCmd(String fileName);

    public abstract String [] getAssembleCmd(String fileName);

    public abstract String [] getExecuteCmd();
}
