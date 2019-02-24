package cs444.codegen.x86.x86_32;

import cs444.codegen.x86.X86OperatingSystem;

class Linux extends X86OperatingSystem<X86_32Platform> {
    Linux(X86_32Platform platform) {
        super("linux", "elf", ".o", "", platform);
    }

    @Override
    public String[] getLinkCmd(String execName) {
        String outputDir = getOutputDir();
        return new String[]{"bash", "-c", "ld -melf_i386 -o " + outputDir + execName + " " + getAllObects()};
    }
}
