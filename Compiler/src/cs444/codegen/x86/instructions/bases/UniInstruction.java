package cs444.codegen.x86.instructions.bases;

import java.util.Arrays;
import java.util.List;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;

public abstract class UniInstruction extends X86Instruction {
    private final String what;
    public final List<InstructionArg<X86Instruction, Size>> data;
    public final Size size;
    public final SizeHelper<X86Instruction, Size> sizeHelper;

    protected UniInstruction(final String what, final InstructionArg<X86Instruction, Size> data, final Size size,
            final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int instSize){
        this(what, Arrays.asList(data), size, sizeHelper, time, instSize);
    }
    
    protected UniInstruction(final String what, final List<InstructionArg<X86Instruction, Size>> data, final Size size,
            final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int instSize){

        super(time, instSize);
        this.what = what;
        this.data = data;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    protected UniInstruction(final String what, final InstructionArg<X86Instruction, Size> data, final SizeHelper<X86Instruction, Size> sizeHelper, final int time, final int size) {
        this(what, data, sizeHelper.getDefaultSize(), sizeHelper, time, size);
    }

    @Override
    public final String generate() {
        StringBuilder sb = new StringBuilder(what).append(" ");
        int i = 0;
        for(;i < data.size() - 2; i++) {
            sb.append(data.get(i).getValue(size, sizeHelper)).append(", ");
        }
        sb.append(data.get(data.size() - 1).getValue(size, sizeHelper));
        
        return sb.toString();
    }

    @Override
    public boolean uses(final InstructionArg<X86Instruction, ?> what) {
        for(InstructionArg<X86Instruction, Size> arg : data) {
            if (arg.uses(what)) return true;
        }
        return false;
    }
}