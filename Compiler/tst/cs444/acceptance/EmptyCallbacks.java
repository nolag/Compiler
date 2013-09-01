package cs444.acceptance;

import java.io.File;
import java.util.Collection;

import cs444.codegen.Platform;

public class EmptyCallbacks implements ITestCallbacks {

    @Override
    public boolean beforeCompile(final File file) {
        return true;
    }

    @Override
    public boolean afterCompile(final File file, final Collection<Platform<?, ?>> platforms) {
        return true;
    }
}
