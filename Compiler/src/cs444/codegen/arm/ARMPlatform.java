package cs444.codegen.arm;

import java.util.Set;

import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.InstructionArg.Size;
import cs444.codegen.arm.instructions.bases.ARMInstruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;

public abstract class ARMPlatform extends Platform<ARMInstruction, Size>{
    public interface ARMPlatformFactory<P extends ARMPlatform> extends PlatformFactory<ARMInstruction, Size, P> {
        @Override
        P getPlatform(Set<String> opts);
    }
    
    private static InstructionHolder<ARMInstruction> genInstructionHolder(final Set<String> options, final ARMSizeHelper sizeHelper){
        return new InstructionPrinter<>();
    }
    
    protected final ARMSizeHelper sizeHelper;
    
    protected ARMPlatform(Set<String> options, String name, IRuntime<ARMInstruction> runtime, 
            TileInit<ARMInstruction, Size> tiles, ARMSizeHelper sizeHelper) {
        super(options, name, runtime, tiles, genInstructionHolder(options, sizeHelper));
        this.sizeHelper = sizeHelper;
    }
    
    @Override
    public ARMSizeHelper getSizeHelper() {
        return sizeHelper;
    }
    
    
}
