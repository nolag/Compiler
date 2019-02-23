package cs444.codegen.x86.peepholes;

import java.io.PrintStream;

import cs444.codegen.Platform;
import cs444.codegen.peepholes.BasicInstrucitonHolder;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class MovZeroRegRemover extends BasicInstrucitonHolder<X86Instruction>{
    private final InstructionHolder<X86Instruction> next;

    public MovZeroRegRemover(final InstructionHolder<X86Instruction> next) {
        this.next = next;
    }

    @Override
    public void flush(final Platform<X86Instruction, ?> platform, final PrintStream printer) {
        next.flush(platform, printer);
    }

    @Override
    public void add(final X86Instruction instruction) {
        if (instruction instanceof Mov) {
            final Mov mov = (Mov) instruction;
            if (mov.dest instanceof Register && mov.src.equals(Immediate.ZERO)) {
                next.add(new Xor((Register)mov.dest, (Register)mov.dest, mov.sizeHelper));
                return;
            }
        }

        next.add(instruction);
    }
}
