package cs444.codegen.x86.x86_64;

import cs444.codegen.x86.X86OperatingSystem;

class X86_64Linux extends X86OperatingSystem<X86_64Platform> {    
    X86_64Linux(X86_64Platform platform) {
        super("linux", "elf64", ".o", "", platform);
    }

    @Override
    public String[] getLinkCmd(final String execName) {
        final String outputDir = getOutputDir();
        return new String[] {"bash", "-c", "ld -melf_x86_64 -o " + outputDir  + execName + " " + getAllObects()};
    }
}
