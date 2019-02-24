package cs444.codegen.x86;

import cs444.codegen.OperatingSystem;

import java.io.File;

public abstract class X86OperatingSystem<T extends X86Platform> extends OperatingSystem<T> {
    private final String assembleName;

    public X86OperatingSystem(String name, String assembleName, String objEnding,
                              String execEnding,
                              T platform) {
        super(name, objEnding, execEnding, platform);
        this.assembleName = assembleName;
    }

    @Override
    public String[] getAssembleCmd(File file) {
        String name = file.getName();
        //add -g (-F "dwarf" also for linux) for debugging
        return new String[]{"nasm", "-O1", "-f", assembleName, file.getAbsolutePath(), "-o",
                getOutputDir() + name.substring(0, name.length() - 2) + objEnding};
    }
}
