package cs444.acceptance;

import cs444.codegen.Platform;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface ITestCallbacks {
    boolean beforeCompile(File file) throws IOException;

    boolean afterCompile(File file, Collection<Platform<?, ?>> platforms) throws IOException,
            InterruptedException;
}
