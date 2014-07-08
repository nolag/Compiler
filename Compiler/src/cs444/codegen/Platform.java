package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs444.Compiler;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class Platform<T extends Instruction<T>, E extends Enum<E>> {
    protected static final String NO_PEEPHOLE = "--no_peep";

    private final InstructionHolder<T> instrucitons;

    protected final IRuntime<T> runtime;

    private final SelectorIndexedTable<T, E> sit;

    private SubtypeIndexedTable<T, E> subtype;

    public interface PlatformFactory<T extends Instruction<T>, E extends Enum<E>, P extends Platform<T, E>> {
        P getPlatform(Set<String> opts);
    }

    private final Set<String> options;
    private final String outDir;

    private final SizeHelper<T, E> sizeHelper;

    protected Platform(final Set<String> options, final String name, final IRuntime<T> runtime, final TileInit<T, E> tiles,
            final InstructionHolder<T> instrucitons, SizeHelper<T, E> sizeHelper) {
        this.options = new HashSet<>(options);
        outDir = Compiler.OUTPUT_DIRECTORY + File.separator + name;
        this.runtime = runtime;
        this.instrucitons = instrucitons;
        this.sizeHelper = sizeHelper;
        sit = new SelectorIndexedTable<>(this);
        tiles.init(options);
    }

    private final Map<ISymbol, InstructionsAndTiming<T>> bests = new HashMap<ISymbol, InstructionsAndTiming<T>>();
    public static final String ENTRY = "entry";

    public final void addBest(final ISymbol symbol, final InstructionsAndTiming<T> tile) {
        bests.put(symbol, tile);
    }

    public final InstructionsAndTiming<T> getBest(final ISymbol symbol) {
        return bests.get(symbol);
    }

    public final CodeGenVisitor<T, E> makeNewCodeGen() {
        return new CodeGenVisitor<T, E>(this);
    }

    //TODO revisit this
    @Override
    public final boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        @SuppressWarnings("unchecked")
        final Platform<T, E> platform = (Platform<T, E>) other;
        return options.equals(platform.options);
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode() ^ options.hashCode();
    }

    public final SizeHelper<T, E> getSizeHelper() {
        return sizeHelper;
    }

    public abstract ObjectLayout<T, E> getObjectLayout();

    public final SelectorIndexedTable<T, E> getSelectorIndex() {
        return sit;
    }

    public final void makeSubtypeTable(final List<APkgClassResolver> resolvers, final boolean outputFile, final String directory)
            throws IOException {
        subtype = new SubtypeIndexedTable<>(this, resolvers, outputFile, directory);
    }

    public final SubtypeIndexedTable<T, E> getSubtypeTable() {
        return subtype;
    }

    public final IRuntime<T> getRunime() {
        return runtime;
    }

    public final InstructionHolder<T> getInstructionHolder() {
        return instrucitons;
    }

    // Note, it is a large refactoring, but the best way to do this is to add P
    // extends Platform<T, E, P> and use P
    public abstract OperatingSystem<? extends Platform<T, E>>[] getOperatingSystems();

    // Functions for file headers
    public final void genStartInstructions(final String methodName, final Addable<T> instructions) {
        instructions.add(makeGlobal(ENTRY));
        instructions.add(makeLabel(ENTRY));
        instructions.add(makeExtern(StaticFieldInit.STATIC_FIELD_INIT_LBL));
        instructions.add(makeCall(StaticFieldInit.STATIC_FIELD_INIT_LBL));
    }

    public abstract void genInstructorInvoke(final APkgClassResolver resolver, final Addable<T> instructions);

    public final void genHeaderStart(final Addable<T> instructions) {
        runtime.externAll(instructions);
        instructions.add(getTextSection());
        instructions.add(makeComment(CodeGenVisitor.INIT_OBJECT_FUNC + ": call super default constructor and initialize obj fields."));
        instructions.add(makeLabel(CodeGenVisitor.INIT_OBJECT_FUNC));
    }

    public abstract void genHeaderEnd(final APkgClassResolver resolver, final Addable<T> instructions);

    public abstract TileSet<T, E> getTiles();

    public void genLayoutForStaticFields(final Iterable<DclSymbol> staticFields, final Addable<T> instructions) {
        if (staticFields.iterator().hasNext()) {
            instructions.add(makeComment("Static fields:"));
            instructions.add(getBSSSection());
        }

        for (final DclSymbol fieldDcl : staticFields) {
            final E size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
            final String fieldLbl = APkgClassResolver.getUniqueNameFor(fieldDcl);
            instructions.add(makeGlobal(fieldLbl));
            instructions.add(makeSpace(fieldLbl, size));
        }
    }

    public abstract TileHelper<T, E> getTileHelper();

    public abstract void zeroDefaultLocation(final Addable<T> instructions);

    public abstract void moveStatic(final String staticLbl, E size, final Addable<T> instructions);

    public abstract void zeroStatic(final String staticLbl, E size, final Addable<T> instructions);

    public abstract void moveStaticLong(final String staticLbl, final Addable<T> instructions);

    public abstract void zeroStaticLong(final String staticLbl, final Addable<T> instructions);

    public abstract T makeComment(final String val);

    public abstract String getNullStr();

    public abstract String getFalseStr();

    public abstract String getTrueStr();

    // Extern is not always needed depending on the assembler
    public abstract T makeExtern(final String what);

    public abstract T makeLabel(final String what);

    public abstract T makeGlobal(final String what);

    public abstract T getDataSection();

    public abstract T getTextSection();

    public abstract T getBSSSection();

    public abstract T makeCall(String what);

    public abstract T getRet();

    public abstract T makeSpace(String name, E size);

    public final String getOutputDir() {
        return outDir;
    }
}
