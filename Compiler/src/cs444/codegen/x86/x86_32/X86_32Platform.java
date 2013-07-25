package cs444.codegen.x86.x86_32;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.Section.SectionType;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ReserveInstructionMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileHelper;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileInit;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class X86_32Platform extends X86Platform{
    public final X86SelectorIndexedTable sit = new X86SelectorIndexedTable(sizeHelper);
    protected final IRuntime<X86Instruction> RUNTIME;
    protected static final X86SizeHelper sizeHelper = X86SizeHelper.sizeHelper32;

    protected X86_32Platform(final IRuntime<X86Instruction> runtime, final Map<String, Boolean> opts){
        super(opts, X86_32TileInit.instance);
        RUNTIME = runtime;
    }

    @Override
    public final IRuntime<X86Instruction> getRunime() {
        return RUNTIME;
    }

    @Override
    public final X86SizeHelper getSizeHelper() {
        return sizeHelper;
    }

    @Override
    public final X86ObjectLayout getObjectLayout() {
        return X86ObjectLayout.object32;
    }

    @Override
    public final X86SelectorIndexedTable getSelectorIndex() {
        return sit;
    }

    @Override
    public final void generateStaticCode(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException {

        StaticFieldInit.generateCode(resolvers, this, outputFile, directory);
    }

    @Override
    public final void genInstructorInvoke(final APkgClassResolver resolver, final Addable<X86Instruction> instructions) {
        X86_32TileHelper.instance.invokeConstructor(resolver, Collections.<ISymbol>emptyList(), this, instructions);
    }

    @Override
    public final void genHeaderStart(final Addable<X86Instruction> instructions) {
        RUNTIME.externAll(instructions);
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

            //TODO may need to make something here for longs not sure if I can just resurve 64 bits.
            final String fieldLbl = APkgClassResolver.getUniqueNameFor(fieldDcl);
            instructions.add(new Global(fieldLbl));
            final ReserveInstruction data = ReserveInstructionMaker.make(fieldLbl, size, 1);
            instructions.add(data);
        }
    }
}