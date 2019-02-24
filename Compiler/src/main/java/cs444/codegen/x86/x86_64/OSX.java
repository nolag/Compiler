package cs444.codegen.x86.x86_64;

import cs444.codegen.x86.X86OperatingSystem;

class OSX extends X86OperatingSystem<X86_64Platform> {
    OSX(X86_64Platform platform) {
        super("osx", "macho64", ".o", "", platform);
    }

    @Override
    public String[] getLinkCmd(String execName) {
        String outputDir = getOutputDir();
        return new String[]{"bash", "-c",
                "ld -macosx_version_min 10.5 -e _start -o " + outputDir + execName + " " + getAllObects()};
    }
}
