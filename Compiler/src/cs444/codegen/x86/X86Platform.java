package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;
import cs444.codegen.x86.instructions.Call;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Global;
import cs444.codegen.x86.instructions.Label;
import cs444.codegen.x86.instructions.Section;
import cs444.codegen.x86.instructions.Xor;
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

    private static final X86Instruction ENTRY_GLOBAL = new Global(Platform.ENTRY);
    private static final X86Instruction ENTRY_LBL = new Label(Platform.ENTRY);
    private static final X86Instruction STATIC_INIT_EXTERN = new Extern(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL));

    protected final X86SizeHelper sizeHelper;

    private static InstructionHolder<X86Instruction> genInstructionHolder(final Set<String> options, final X86SizeHelper sizeHelper) {
        final InstructionHolder<X86Instruction> printer = new InstructionPrinter<>();
        if (options.contains(NO_PEEPHOLE)) {
            return printer;
        } else {
            final InstructionHolder<X86Instruction> pushPop = new PushPopRemover(printer, sizeHelper);
            return new MovZeroRegRemover(pushPop);
        }
    }

    protected X86Platform(final Set<String> options, final TileInit<X86Instruction, Size> tiles, final IRuntime<X86Instruction> runtime,
            final X86SizeHelper sizeHelper, final String name) {
        super(options, name, runtime, tiles, genInstructionHolder(options, sizeHelper), sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final void genHeaderStart(final Addable<X86Instruction> instructions) {
        runtime.externAll(instructions);
        instructions.add(Section.TEXT);
        instructions.add(new Comment(CodeGenVisitor.INIT_OBJECT_FUNC + ": call super default constructor and initialize obj fields."
                + " eax should contain address of object."));
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
            instructions.add(Section.BSS);
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
    public final void generateStaticCode(final List<APkgClassResolver> resolvers, final boolean outputFile, final String directory)
            throws IOException {

        StaticFieldInit.generateCode(resolvers, this, outputFile, directory);
    }

    @Override
    public X86Instruction comment(final String value) {
        return new Comment(value);
    }

    @Override
    public String getNullStr() {
        return Immediate.NULL.toString();
    }

    @Override
    public String getFalseStr() {
        return Immediate.FALSE.toString();
    }

    @Override
    public String getTrueStr() {
        return Immediate.TRUE.toString();
    }

    @Override
    public Extern getExtern(final String what) {
        return new Extern(what);
    }

    @Override
    public Label getLabel(final String what) {
        return new Label(what);
    }

    @Override
    public Global getGlobal(final String what) {
        return new Global(what);
    }

    @Override
    public Section getDataSection() {
        return Section.DATA;
    }
}
