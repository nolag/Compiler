package cs444.acceptance;

import cs444.codegen.Platform;

import java.io.File;
import java.util.Collection;

public class EmptyCallbacks implements ITestCallbacks {

    @Override
    public boolean beforeCompile(File file) {
        return true;
    }

    @Override
    public boolean afterCompile(File file, Collection<Platform<?, ?>> platforms) {
        return true;
    }
}
