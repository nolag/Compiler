package cs444.codegen.arm;

import cs444.codegen.OperatingSystem;

import java.io.File;

public abstract class ArmOperatingSystem<T extends ArmPlatform> extends OperatingSystem<T> {

    public ArmOperatingSystem(String name, String assembleName, String objEnding,
                              String execEnding,
                              T platform) {
        super(name, objEnding, execEnding, platform);
    }

    //TODO see if there is a way to say file format (like elf) and platform (like a32 or whatever version I support)
    @Override
    public String[] getAssembleCmd(File file) {
        String name = file.getName();
        return new String[]{"as", "-g", file.getAbsolutePath(), "-o", getOutputDir() + name.substring(0,
                name.length() - 2) + objEnding};
    }
}
