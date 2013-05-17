package cs444.codegen.x86_32.linux;

import cs444.codegen.x86_32.X86_32Platform;



public class X86_32LinuxPlatform extends X86_32Platform{
    public static X86_32LinuxPlatform platform = new X86_32LinuxPlatform();
    private X86_32LinuxPlatform(){ }

    @Override
    public Runtime getRunime() {
        return Runtime.instance;
    }
}
