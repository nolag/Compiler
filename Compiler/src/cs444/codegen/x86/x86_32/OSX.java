package cs444.codegen.x86.x86_32;

import cs444.codegen.x86.X86OperatingSystem;

class OSX extends X86OperatingSystem<X86_32Platform> {
    OSX(X86_32Platform platform) {
        super("osx", "macho", ".o", "", platform);
    }

    @Override
    public String[] getLinkCmd(final String execName) {
        final String outputDir = getOutputDir();
        return new String[] {"bash", "-c", "ld -macosx_version_min 10.4 -e _start -o " + outputDir  + execName + " " + getAllObects()};
    }
}
