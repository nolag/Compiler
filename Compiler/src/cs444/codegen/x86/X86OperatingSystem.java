package cs444.codegen.x86;

import java.io.File;

import cs444.codegen.OperatingSystem;

public abstract class X86OperatingSystem<T extends X86Platform> extends OperatingSystem<T> {
    private final String assembleName;
    
    public X86OperatingSystem(final String name, final String assembleName, 
            final String objEnding, final String execEnding, final T platform) {
        super(name, objEnding, execEnding, platform);
        this.assembleName = assembleName;
    }
    
    @Override
    public String[] getAssembleCmd(File file) {
        final String name = file.getName();
        //add -g (-F "dwarf" also for linux) for debugging
        return new String[] {"nasm", "-O1", "-f", assembleName, file.getAbsolutePath(), "-o", 
                getOutputDir() + name.substring(0, name.length() - 2) + objEnding};
    }
}
