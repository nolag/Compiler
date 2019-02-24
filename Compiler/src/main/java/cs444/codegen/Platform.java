package cs444.codegen;

import cs444.Compiler;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class Platform<T extends Instruction<T>, E extends Enum<E>> {
    public static final String ENTRY = "entry";
    protected static final String NO_PEEPHOLE = "--no_peep";
    protected final IRuntime<T> runtime;
    private final InstructionHolder<T> instructions;
    private final SelectorIndexedTable<T, E> sit;
    private final Set<String> options;
    private final String outDir;
    private final SizeHelper<T, E> sizeHelper;
    private final Map<ISymbol, InstructionsAndTiming<T>> bests = new HashMap<ISymbol, InstructionsAndTiming<T>>();
    private SubtypeIndexedTable<T, E> subtype;

    protected Platform(Set<String> options, String name, IRuntime<T> runtime,
                       TileInit<T, E> tiles,
                       InstructionHolder<T> instrucitons, SizeHelper<T, E> sizeHelper) {
        this.options = new HashSet<>(options);
        outDir = Compiler.OUTPUT_DIRECTORY + File.separator + name;
        this.runtime = runtime;
        instructions = instrucitons;
        this.sizeHelper = sizeHelper;
        sit = new SelectorIndexedTable<>(this);
        tiles.init(options);
    }

    public final void addBest(ISymbol symbol, InstructionsAndTiming<T> tile) {
        bests.put(symbol, tile);
    }

    public final InstructionsAndTiming<T> getBest(ISymbol symbol) {
        return bests.get(symbol);
    }

    public final CodeGenVisitor<T, E> makeNewCodeGen() {
        return new CodeGenVisitor<T, E>(this);
    }

    //TODO revisit this
    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked") Platform<T, E> platform = (Platform<T, E>) other;
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

    public final void makeSubtypeTable(List<APkgClassResolver> resolvers, boolean outputFile,
                                       String directory)
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
        return instructions;
    }

    // Note, it is a large refactoring, but the best way to do this is to add P
    // extends Platform<T, E, P> and use P
    public abstract OperatingSystem<? extends Platform<T, E>>[] getOperatingSystems();

    // Functions for file headers
    public final void genStartInstructions(String methodName, Addable<T> instructions) {
        TileHelper<T, E> helper = getTileHelper();
        instructions.add(makeGlobal(ENTRY));
        instructions.add(makeLabel(ENTRY));
        instructions.add(makeExtern(StaticFieldInit.STATIC_FIELD_INIT_LBL));
        helper.setupStaticHeader(instructions, sizeHelper);
        instructions.add(makeCall(StaticFieldInit.STATIC_FIELD_INIT_LBL));
        helper.setupStaticFooter(instructions, sizeHelper);
    }

    public abstract void genInstructorInvoke(APkgClassResolver resolver, Addable<T> instructions);

    public void getEnterStaticField(Addable<T> instructions) {
        instructions.add(getTextSection());
        instructions.add(makeGlobal(StaticFieldInit.STATIC_FIELD_INIT_LBL));
        instructions.add(makeLabel(StaticFieldInit.STATIC_FIELD_INIT_LBL));
        runtime.externAll(instructions);
    }

    public void genHeaderStart(Addable<T> instructions) {
        runtime.externAll(instructions);
        instructions.add(getTextSection());
        instructions.add(makeComment(CodeGenVisitor.INIT_OBJECT_FUNC + ": call super default constructor and " +
                "initialize obj fields."));
        instructions.add(makeLabel(CodeGenVisitor.INIT_OBJECT_FUNC));
    }

    public abstract void genHeaderEnd(APkgClassResolver resolver, Addable<T> instructions);

    public abstract TileSet<T, E> getTiles();

    public void genLayoutForStaticFields(Iterable<DclSymbol> staticFields, Addable<T> instructions) {
        if (staticFields.iterator().hasNext()) {
            instructions.add(makeComment("Static fields:"));
            instructions.add(getBSSSection());
        }

        for (DclSymbol fieldDcl : staticFields) {
            E size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
            String fieldLbl = APkgClassResolver.getUniqueNameFor(fieldDcl);
            instructions.add(makeGlobal(fieldLbl));
            instructions.addAll(makeSpace(fieldLbl, size));
        }
    }

    public abstract TileHelper<T, E> getTileHelper();

    public abstract void zeroDefaultLocation(Addable<T> instructions);

    public abstract void moveStatic(String staticLbl, E size, Addable<T> instructions);

    public abstract void zeroStatic(String staticLbl, E size, Addable<T> instructions);

    public abstract void moveStaticLong(String staticLbl, Addable<T> instructions);

    public abstract void zeroStaticLong(String staticLbl, Addable<T> instructions);

    public abstract T makeComment(String val);

    public abstract String getNullStr();

    public abstract String getFalseStr();

    public abstract String getTrueStr();

    //TODO make these like the rest and just take Addable as an arg.  More consistant and won't need to return T[] ever
    // Extern is not always needed depending on the assembler
    public abstract T makeExtern(String what);

    public abstract T makeLabel(String what);

    public abstract T makeGlobal(String what);

    public abstract T getDataSection();

    public abstract T getTextSection();

    public abstract T getBSSSection();

    public abstract T makeCall(String what);

    public abstract T getRetStaticField();

    public abstract T[] makeSpace(String name, E size);

    public final String getOutputDir() {
        return outDir;
    }

    public interface PlatformFactory<T extends Instruction<T>, E extends Enum<E>, P extends Platform<T, E>> {
        P getPlatform(Set<String> opts);
    }
}
