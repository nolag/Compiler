package cs444.codegen.x86.x86_64.windows;

import java.io.File;
import java.util.Set;

import cs444.Compiler;
import cs444.codegen.Addable;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.StaticFieldInit;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_64.Runtime;
import cs444.codegen.x86.x86_64.X86_64Platform;

public class X86_64WindowsPlatform extends X86_64Platform {
    public static class Factory implements X86PlatformFactory<X86_64WindowsPlatform>{
        public static Factory factory = new Factory();

        private Factory(){ }

        @Override
        public X86_64WindowsPlatform getPlatform(final Set<String> opts){
            return new X86_64WindowsPlatform(opts);
        }
    }

    private static final Immediate EXIT = new Immediate("ExitProcess");

    public X86_64WindowsPlatform(final Set<String> opts){
        super(Runtime.instance, opts);
    }

    @Override
    public final void genStartInstructions(final String methodName, final Addable<X86Instruction> instructions) {
        instructions.add(new Global("main"));
        instructions.add(new Label("main"));
        instructions.add(new Extern(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL)));
        instructions.add(new Call(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL), sizeHelper));
        instructions.add(new Call(new Immediate(methodName), sizeHelper));
        instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Comment("adding shadow space, no need to remove it at end because process will exit"));
        instructions.add(new Sub(Register.STACK, Immediate.THIRTY_TWO, sizeHelper));
        instructions.add(new Extern(EXIT));
        instructions.add(new Call(EXIT, sizeHelper));
    }

    @Override
    public String getOutputDir() {
        return Compiler.OUTPUT_DIRECTORY + "x64w/";
    }

    //Test methods

    private static final String [] execute = new String[] {Compiler.OUTPUT_DIRECTORY + "x64w/main"};

    //NOTE: this line must be changed to YOUR location of kernel32.lib.  Yes Kernel32 is the name of the 64 bit kernel...
    private static final String KERNEL32 = "\"C:\\Program Files (x86)\\Microsoft SDKs\\Windows\\v7.1A\\Lib\\x64\\Kernel32.lib\"";

    @Override
    public String[] getLinkCmd(final String fileName) {
        return new String[] {"link", "/subsystem:console", "/machine:x64", "/nodefaultlib", "/entry:main",
                /*TODO fix this so it's not needed*/ "/LARGEADDRESSAWARE:NO",
                "/out:\"" + getOutputDir() + "main.exe\"",  KERNEL32, fileName + File.separator + "*.obj"};
    }

    @Override
    public String [] getExecuteCmd() {
        return execute;
    }

    @Override
    public String[] getAssembleCmd(final String fileName) {
        //return new String[] {"nasm", "-O1", "-f", "win64", fileName};
        //This will add debugging to the compiled objects.  Use it for debugging failed tests
        return new String[] {"nasm", "-O1", "-f", "win64", "-g", fileName};
    }
}
