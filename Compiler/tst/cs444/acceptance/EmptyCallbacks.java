package cs444.acceptance;

import java.io.File;

public class EmptyCallbacks implements ITestCallbacks {

    @Override
    public boolean beforeCompile(File file) {
        return true;
    }

    @Override
    public boolean afterCompile(File file) {
        return true;
    }
}
