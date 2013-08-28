package cs444.codegen.x86.x86_32.linux;

import java.util.Map;

import cs444.codegen.Addable;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.StaticFieldInit;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileHelper;

public class X86_32LinuxPlatform extends X86_32Platform{
    public static X86_32LinuxPlatform platform;

    private static final Immediate EXIT = Immediate.ONE;
    private static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");

    private X86_32LinuxPlatform(final Map<String, Boolean> opts){
        super(Runtime.instance, opts);
    }

    //NOTE this is for testing
    public static void reset(final Map<String, Boolean> opts){
        platform = new X86_32LinuxPlatform(opts);
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
    public TileHelper<X86Instruction, Size> getTileHelper() {
        return X86_32TileHelper.instance;
    }
}
