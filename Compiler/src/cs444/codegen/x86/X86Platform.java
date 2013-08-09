package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cs444.codegen.*;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.peephole.InstructionPrinter;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.Section.SectionType;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ReserveInstructionMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class X86Platform extends Platform<X86Instruction, Size>{

    private final InstructionHolder<X86Instruction> instrucitons;
    protected final IRuntime<X86Instruction> runtime;
    protected final X86SizeHelper sizeHelper;
    private SubtypeIndexedTable<X86Instruction, Size> subtype;

    protected X86Platform(final Map<String, Boolean> options, final TileInit tiles,
            final IRuntime<X86Instruction> runtime, final X86SizeHelper sizeHelper){

        instrucitons = new InstructionPrinter<X86Instruction>();
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
                subtype = new X86SubtypeIndexedTable(resolvers, outputFile, directory, getSizeHelper());
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
    public final void genLayoutForStaticFields(final Iterable<DclSymbol> staticFields, final Addable<X86Instruction> instructions){
        if (staticFields.iterator().hasNext()){
            instructions.add(new Comment("Static fields:"));
            instructions.add(new Section(SectionType.BSS));
        }

        for (final DclSymbol fieldDcl : staticFields) {
            final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
            final String fieldLbl = APkgClassResolver.getUniqueNameFor(fieldDcl);
            if(fieldDcl.getType().value.equals(JoosNonTerminal.LONG)){
                instructions.add(new Resd(fieldLbl + "_high", 1));
                instructions.add(new Global(fieldLbl));
                instructions.add(new Resd(fieldLbl, 1));

            }else{
                instructions.add(new Global(fieldLbl));
                final ReserveInstruction data = ReserveInstructionMaker.make(fieldLbl, size);
                instructions.add(data);
            }
        }
    }

    @Override
    public final TileSet<X86Instruction, Size> getTiles(){
        return TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class);
    }
}
