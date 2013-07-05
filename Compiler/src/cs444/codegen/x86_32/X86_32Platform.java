package cs444.codegen.x86_32;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.Section;
import cs444.codegen.instructions.x86.Section.SectionType;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.*;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.types.APkgClassResolver;

public abstract class X86_32Platform extends X86Platform{
    public final X86SelectorIndexedTable sit = new X86SelectorIndexedTable(X86SizeHelper.sizeHelper32);
    protected final IRuntime<X86Instruction> RUNTIME;

    protected X86_32Platform(final IRuntime<X86Instruction> runtime){
        RUNTIME = runtime;
    }

    @Override
    public final IRuntime<X86Instruction> getRunime() {
        return RUNTIME;
    }

    @Override
    public final X86SizeHelper getSizeHelper() {
        return X86SizeHelper.sizeHelper32;
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
        TileHelper.invokeConstructor(resolver, Collections.<ISymbol>emptyList(), this, instructions);
    }

    @Override
    public final void genHeaderStart(final Addable<X86Instruction> instructions) {
        RUNTIME.externAll(instructions);
        instructions.add(new Section(SectionType.TEXT));
        instructions.add(new Comment(CodeGenVisitor.INIT_OBJECT_FUNC + ": call super default constructor and initialize obj fields." +
                " eax should contain address of object."));
        instructions.add(new Label(CodeGenVisitor.INIT_OBJECT_FUNC));
    }
}