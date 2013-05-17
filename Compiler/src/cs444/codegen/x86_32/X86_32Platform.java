package cs444.codegen.x86_32;

import java.io.IOException;
import java.util.List;

import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.peephole.InstructionPrinter;
import cs444.codegen.x86.StaticFieldInit;
import cs444.codegen.x86.X86ObjectLayout;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.X86SelectorIndexedTable;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.X86SubtypeIndexedTable;
import cs444.types.APkgClassResolver;

public abstract class X86_32Platform implements X86Platform{
    public final X86SelectorIndexedTable sit = new X86SelectorIndexedTable(X86SizeHelper.sizeHelper32);
    private final InstructionHolder<X86Instruction> instrucitons;
    private SubtypeIndexedTable<X86Instruction> subtype;


    protected X86_32Platform(){
        instrucitons = new InstructionPrinter<X86Instruction>();
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
    public final void makeSubtypeTable(final List<APkgClassResolver> resolvers, final boolean outputFile, final String directory)
            throws IOException {
                subtype = new X86SubtypeIndexedTable(resolvers, outputFile, directory);
            }

    @Override
    public final SubtypeIndexedTable<X86Instruction> getSubtypeTable() {
        return subtype;
    }

    @Override
    public final InstructionHolder<X86Instruction> getInstructionHolder() {
        return instrucitons;
    }

    @Override
    public void generateStaticCode(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException {

        StaticFieldInit.generateCode(resolvers, this, outputFile, directory);
    }
}