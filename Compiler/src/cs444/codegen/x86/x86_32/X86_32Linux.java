package cs444.codegen.x86.x86_32;

import cs444.codegen.x86.X86OperatingSystem;

class X86_32Linux extends X86OperatingSystem<X86_32Platform> {
    X86_32Linux(X86_32Platform platform) {
        super("linux", "elf", ".o", "", platform);
    }

    @Override
    public String[] getLinkCmd(final String execName) {
        final String outputDir = getOutputDir();
        return new String[] {"bash", "-c", "ld -melf_i386 -o " + outputDir  + execName + " " + getAllObects()};
    }
}
