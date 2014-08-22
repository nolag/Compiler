package cs444.codegen.arm;

import java.io.File;

import cs444.codegen.OperatingSystem;

public abstract class ArmOperatingSystem<T extends ArmPlatform> extends OperatingSystem<T> {

    public ArmOperatingSystem(final String name, final String assembleName, final String objEnding, final String execEnding,
            final T platform) {
        super(name, objEnding, execEnding, platform);
    }

    //TODO see if there is a way to say file format (like elf) and platform (like a32 or whatever version I support)
    @Override
    public String[] getAssembleCmd(File file) {
        return new String[] { "gcc", "-c", file.getAbsolutePath(), "-o", getOutputDir() + name.substring(0, name.length() - 2) + objEnding };
    }
}
