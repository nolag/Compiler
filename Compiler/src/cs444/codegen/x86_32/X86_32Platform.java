package cs444.codegen.x86_32;

import java.io.IOException;
import java.util.List;

import cs444.codegen.x86.StaticFieldInit;
import cs444.codegen.x86.X86ObjectLayout;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.X86SelectorIndexedTable;
import cs444.codegen.x86.X86SizeHelper;
import cs444.types.APkgClassResolver;

public abstract class X86_32Platform extends X86Platform{
    public final X86SelectorIndexedTable sit = new X86SelectorIndexedTable(X86SizeHelper.sizeHelper32);

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
    public void generateStaticCode(final List<APkgClassResolver> resolvers,
            final boolean outputFile, final String directory) throws IOException {

        StaticFieldInit.generateCode(resolvers, this, outputFile, directory);
    }
}