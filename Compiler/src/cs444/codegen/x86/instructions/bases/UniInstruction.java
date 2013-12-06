package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;

public abstract class UniInstruction extends X86Instruction {
    private final String what;
    public final InstructionArg [] data;
    public final Size size;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected UniInstruction(final String what, final InstructionArg data, final Size size,
            final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int instSize){
        this(what, new InstructionArg [] { data }, size, sizeHelper, time, instSize);
    }
    
    protected UniInstruction(final String what, final InstructionArg data [], final Size size,
            final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int instSize){

        super(time, instSize);
        this.what = what;
        this.data = data;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    protected UniInstruction(final String what, final InstructionArg data, final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int size) {
        this(what, data, sizeHelper.getDefaultSize(), sizeHelper, time, size);
    }

    @Override
    public final String generate() {
        StringBuilder sb = new StringBuilder(what).append(" ");
        int i = 0;
        for(;i < data.length - 2; i++) {
            sb.append(data[i].getValue(size, sizeHelper)).append(", ");
        }
        sb.append(data[data.length - 1].getValue(size, sizeHelper));
        
        return sb.toString();
    }

    @Override
    public boolean uses(final InstructionArg what) {
        for(InstructionArg arg : data) {
            if (arg.uses(what)) return true;
        }
        return false;
    }
}