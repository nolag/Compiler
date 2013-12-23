package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cs444.codegen.*;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.Section.SectionType;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ReserveInstructionMaker;
import cs444.codegen.x86.peepholes.MovZeroRegRemover;
import cs444.codegen.x86.peepholes.PushPopRemover;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class X86Platform extends Platform<X86Instruction, Size> {

    public interface X86PlatformFactory<P extends X86Platform> extends PlatformFactory<X86Instruction, Size, P> {
        @Override
        P getPlatform(Set<String> opts);
    }
    
    private static final String ENTRY = "entry";
    private static final X86Instruction ENTRY_GLOBAL = new Global(ENTRY);
    private static final X86Instruction ENTRY_LBL = new Label(ENTRY);
    private static final X86Instruction STATIC_INIT_EXTERN = new Extern(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL));

    private final InstructionHolder<X86Instruction> instrucitons;
    protected final IRuntime<X86Instruction> runtime;
    protected final X86SizeHelper sizeHelper;
    private SubtypeIndexedTable<X86Instruction, Size> subtype;

    protected X86Platform(final Set<String> options, final TileInit<X86Instruction, Size> tiles, final IRuntime<X86Instruction> runtime,
            final X86SizeHelper sizeHelper, final String name) {
        super(options, name);

        final InstructionHolder<X86Instruction> printer = new InstructionPrinter<>();

        if(options.contains(NO_PEEPHOLE)) {
            instrucitons = printer;
        } else {
            final InstructionHolder<X86Instruction> pushPop = new PushPopRemover(printer, sizeHelper);
            instrucitons = new MovZeroRegRemover(pushPop);
        }

        tiles.init(options);
        this.runtime = runtime;
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final X86SizeHelper getSizeHelper() {
        return sizeHelper;
    }

    @Override
    public abstract X86ObjectLayout getObjectLayout();

    @Override
    public abstract X86SelectorIndexedTable getSelectorIndex();

    @Override
    public final IRuntime<X86Instruction> getRunime() {
        return runtime;
    }

    @Override
    public final InstructionHolder<X86Instruction> getInstructionHolder() {
        return instrucitons;
    }

    @Override
    public final void makeSubtypeTable(final List<APkgClassResolver> resolvers, final boolean outputFile,
            final String directory) throws IOException {
                subtype = new X86SubtypeIndexedTable(this, resolvers, outputFile, directory, getSizeHelper());
    }

    @Override
    public final SubtypeIndexedTable<X86Instruction, Size> getSubtypeTable() {
        return subtype;
    }

    @Override
    public final void genHeaderStart(final Addable<X86Instruction> instructions) {
        runtime.externAll(instructions);
        instructions.add(new Section(SectionType.TEXT));
        instructions.add(new Comment(CodeGenVisitor.INIT_OBJECT_FUNC + ": call super default constructor and initialize obj fields." +
                " eax should contain address of object."));
        instructions.add(new Label(CodeGenVisitor.INIT_OBJECT_FUNC));
    }

    @Override
    public final void zeroDefaultLocation(final Addable<X86Instruction> instructions) {
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
    }

    @Override
    public final void genLayoutForStaticFields(final Iterable<DclSymbol> staticFields, final Addable<X86Instruction> instructions) {
        if (staticFields.iterator().hasNext()) {
            instructions.add(new Comment("Static fields:"));
            instructions.add(new Section(SectionType.BSS));
        }

        for (final DclSymbol fieldDcl : staticFields) {
            final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
            final String fieldLbl = APkgClassResolver.getUniqueNameFor(fieldDcl);
            instructions.add(new Global(fieldLbl));
            final ReserveInstruction data = ReserveInstructionMaker.make(fieldLbl, size);
            instructions.add(data);
        }
    }
    
    @Override
    public final void genStartInstructions(final String methodName, final Addable<X86Instruction> instructions) {
        instructions.add(ENTRY_GLOBAL);
        instructions.add(ENTRY_LBL);
        instructions.add(STATIC_INIT_EXTERN);
        instructions.add(new Call(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL), sizeHelper));
    }
    
    @Override
    public abstract OperatingSystem<? extends X86Platform>[] getOperatingSystems();
}
