package cs444.codegen.arm.arm32;

import cs444.codegen.arm.ArmOperatingSystem;

public class Linux extends ArmOperatingSystem<Arm32Platform> {
    Linux(Arm32Platform platform) {
        super("linux", "elf", ".o", "", platform);
    }

    @Override
    public String[] getLinkCmd(final String execName) {
        final String outputDir = getOutputDir();
        return new String[] { "bash", "-c", "ld -marmelf_linux_eabi  -o " + outputDir + execName + " " + getAllObects() };
    }
}
