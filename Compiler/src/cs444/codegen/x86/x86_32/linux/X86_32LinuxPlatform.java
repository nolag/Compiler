package cs444.codegen.x86.x86_32.linux;

import java.io.File;
import java.util.Set;

import cs444.Compiler;
import cs444.codegen.Addable;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.StaticFieldInit;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.Runtime;
import cs444.codegen.x86.x86_32.X86_32Platform;

public class X86_32LinuxPlatform extends X86_32Platform {
    public static class Factory implements X86PlatformFactory<X86_32LinuxPlatform>{
        public static Factory factory = new Factory();

        private Factory(){ }

        @Override
        public X86_32LinuxPlatform getPlatform(final Set<String> opts){
            return new X86_32LinuxPlatform(opts);
        }
    }

    private static final Immediate EXIT = Immediate.ONE;
    private static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");

    public X86_32LinuxPlatform(final Set<String> opts){
        super(Runtime.instance, opts);
    }

    @Override
    public final void genStartInstructions(final String methodName, final Addable<X86Instruction> instructions) {
        instructions.add(new Global("_start"));
        instructions.add(new Label("_start"));
        instructions.add(new Extern(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL)));
        instructions.add(new Call(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL), sizeHelper));
        instructions.add(new Call(new Immediate(methodName), sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, EXIT, sizeHelper));
        instructions.add(new Int(SOFTWARE_INTERUPT, sizeHelper));
    }

    @Override
    public String getOutputDir() {
        return Compiler.OUTPUT_DIRECTORY + "x86l/";
    }

    //Test methods

    private static final String [] execute = new String[] {Compiler.OUTPUT_DIRECTORY + "x86l/main"};

    @Override
    public String[] getLinkCmd(final String fileName) {
        return new String[] {"bash", "-c", "ld -melf_i386 -o " + getOutputDir() + "main " + fileName + File.separator + "*.o"};
    }

    @Override
    public String [] getExecuteCmd() {
        return execute;
    }

    @Override
    public String[] getAssembleCmd(final String fileName) {
        return new String[] {"nasm", "-O1", "-f", "elf", fileName};
        //This will add debugging to the compiled objects.  Use it for debugging failed tests
        //return new String[] {"nasm", "-O1", "-f", "elf", "-g", "-F", "dwarf", fileName};
    }
}
