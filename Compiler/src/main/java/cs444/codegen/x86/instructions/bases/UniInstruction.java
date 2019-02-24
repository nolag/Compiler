package cs444.codegen.x86.instructions.bases;

import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.InstructionArg;
import cs444.codegen.x86.Size;

import java.util.Arrays;
import java.util.List;

public abstract class UniInstruction extends X86Instruction {
    public final List<InstructionArg<X86Instruction, Size>> data;
    public final Size size;
    public final SizeHelper<X86Instruction, Size> sizeHelper;
    private final String what;

    protected UniInstruction(String what, InstructionArg<X86Instruction, Size> data, Size size,
                             SizeHelper<X86Instruction, Size> sizeHelper, int time, int instSize) {
        this(what, Arrays.asList(data), size, sizeHelper, time, instSize);
    }

    protected UniInstruction(String what, List<InstructionArg<X86Instruction, Size>> data, Size size,
                             SizeHelper<X86Instruction, Size> sizeHelper, int time, int instSize) {

        super(time, instSize);
        this.what = what;
        this.data = data;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    protected UniInstruction(String what, InstructionArg<X86Instruction, Size> data,
                             SizeHelper<X86Instruction, Size> sizeHelper, int time, int size) {
        this(what, data, sizeHelper.getDefaultSize(), sizeHelper, time, size);
    }

    @Override
    public final String generate() {
        StringBuilder sb = new StringBuilder(what).append(" ");
        int i = 0;
        for (; i < data.size() - 2; i++) {
            sb.append(data.get(i).getValue(size, sizeHelper)).append(", ");
        }
        sb.append(data.get(data.size() - 1).getValue(size, sizeHelper));

        return sb.toString();
    }

    @Override
    public boolean uses(InstructionArg<X86Instruction, ?> what) {
        for (InstructionArg<X86Instruction, Size> arg : data) {
            if (arg.uses(what)) {
                return true;
            }
        }
        return false;
    }
}