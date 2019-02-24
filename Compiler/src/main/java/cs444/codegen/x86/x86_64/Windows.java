package cs444.codegen.x86.x86_64;

import cs444.codegen.x86.X86OperatingSystem;

class Windows extends X86OperatingSystem<X86_64Platform> {
    //NOTE: this line must be changed to YOUR location of kernel32.lib.
    private static final String KERNEL32 = "\"C:\\Program Files (x86)\\Microsoft SDKs\\Windows\\v7" +
            ".1A\\Lib\\x64\\Kernel32.lib\"";

    Windows(X86_64Platform platform) {
        super("windows", "win64", ".obj", ".exe", platform);
    }

    @Override
    public String[] getLinkCmd(String execName) {
        return new String[]{"link", "/subsystem:console", "/machine:x64", "/nodefaultlib", "/entry:main",
                "/out:\"" + getOutputDir() + execName + ".exe\"", KERNEL32, getAllObects()};
    }
}
