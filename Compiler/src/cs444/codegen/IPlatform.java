package cs444.codegen;

import java.io.IOException;
import java.util.List;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.tiles.TileSet;
import cs444.types.APkgClassResolver;

public interface IPlatform<T extends Instruction> {
    public SizeHelper<T> getSizeHelper();
    public ObjectLayout<T> getObjectLayout();
    public SelectorIndexedTable<T> getSelectorIndex();
    public void makeSubtypeTable(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException;

    public SubtypeIndexedTable<T> getSubtypeTable();
    public IRuntime<T> getRunime();
    public InstructionHolder<T> getInstructionHolder();
    public void generateStaticCode(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException;

    public TileSet<T> getTiles();
}
