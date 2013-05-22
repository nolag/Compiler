package cs444.codegen.x86;

import java.io.IOException;
import java.util.List;

import cs444.codegen.IPlatform;
import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.peephole.InstructionPrinter;
import cs444.codegen.tiles.TileSet;
import cs444.types.APkgClassResolver;

public abstract class X86Platform implements IPlatform<X86Instruction>{
    private final InstructionHolder<X86Instruction> instrucitons;
    private SubtypeIndexedTable<X86Instruction> subtype;

    protected X86Platform(){
        instrucitons = new InstructionPrinter<X86Instruction>();
    }

    @Override
    public abstract X86SizeHelper getSizeHelper();

    @Override
    public abstract X86ObjectLayout getObjectLayout();

    @Override
    public abstract X86SelectorIndexedTable getSelectorIndex();

    @Override
    public final InstructionHolder<X86Instruction> getInstructionHolder() {
        return instrucitons;
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
    public TileSet<X86Instruction> getTiles(){
        return TileSet.<X86Instruction>getOrMake(X86Instruction.class);
    }
}
