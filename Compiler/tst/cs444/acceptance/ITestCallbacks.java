package cs444.acceptance;

import java.io.File;
import java.io.IOException;

public interface ITestCallbacks {
    boolean beforeCompile(File file);
    boolean afterCompile(File file) throws IOException, InterruptedException;
}
