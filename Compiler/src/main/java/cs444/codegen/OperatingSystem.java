package cs444.codegen;

import java.io.File;

public abstract class OperatingSystem<T extends Platform<?, ?>> {
    public final String name;

    protected final T platform;
    protected final String objEnding;
    protected final String execEnding;

    private final File runtimeFile;

    public OperatingSystem(String name, String objEnding, String execEnding, T platform) {
        this.name = name;
        this.platform = platform;
        runtimeFile = new File(getOutputDir(), "runtime.s");
        this.objEnding = objEnding;
        this.execEnding = execEnding;
    }

    public final String getObjName(File assemblyFile) {
        String name = assemblyFile.getName();
        return name.substring(0, name.length() - 1) + objEnding;
    }

    public final String getOutputDir() {
        return platform.getOutputDir() + File.separator + name + File.separator;
    }

    public final String getAllObects() {
        return getOutputDir() + "*" + objEnding;
    }

    public final File getRuntimeFile() {
        return runtimeFile;
    }

    //Used for testing
    public abstract String[] getAssembleCmd(File file);

    public abstract String[] getLinkCmd(String execName);

    public String[] getExecuteCmd(String execName) {
        return new String[]{getOutputDir() + execName + execEnding};
    }
}
