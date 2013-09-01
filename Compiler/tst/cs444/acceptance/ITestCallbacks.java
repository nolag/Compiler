package cs444.acceptance;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import cs444.codegen.Platform;

public interface ITestCallbacks {
    boolean beforeCompile(File file) throws IOException;
    boolean afterCompile(File file, final Collection<Platform<?, ?>> platforms) throws IOException, InterruptedException;
}
