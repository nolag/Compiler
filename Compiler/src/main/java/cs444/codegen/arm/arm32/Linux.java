package cs444.codegen.arm.arm32;

import cs444.codegen.arm.ArmOperatingSystem;

import java.util.Optional;

public class Linux extends ArmOperatingSystem<Arm32Platform> {
    Linux(Arm32Platform platform) {
        super("linux", "elf", ".o", "", platform);
    }

    /* If testing on real arm, comment this out.  This allows testing alongside other platforms */
    @Override
    protected Optional<String> getEmulationCmd() {
        return Optional.of("qemu-arm");
    }

    @Override
    public String[] getLinkCmd(String execName) {
        String outputDir = getOutputDir();
        return new String[]{"bash", "-c", "ld -marmelf_linux_eabi  -o " + outputDir + execName + " " + getAllObects()};
    }
}
