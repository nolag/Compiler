package cs444.codegen.x86.peepholes;

import java.io.PrintStream;
import java.util.*;

import cs444.codegen.SizeHelper;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;

public class PushPopRemover implements InstructionHolder<X86Instruction> {
    private final Deque<List<X86Instruction>> instructions = new ArrayDeque<>();
    private final InstructionHolder<X86Instruction> next;
    private final SizeHelper<X86Instruction, Size> sizeHelper;

    public PushPopRemover(final InstructionHolder<X86Instruction> next, final SizeHelper<X86Instruction, Size> sizeHelper) {
        this.next = next;
        this.sizeHelper = sizeHelper;
    }

    private void reduceIfPosible(final Pop pop) {
        final Register to = (Register) pop.data;
        final List<X86Instruction> latest = instructions.removeLast();
        final Push push = (Push) latest.remove(0);
        final Register pushReg = (Register) push.data;

        boolean usedReg = false;
        for (final X86Instruction instruction : latest) {
            if (instruction.writesTo(to)) {
                usedReg = true;
                break;
            }
        }

        if (!usedReg) {
            if (to != pushReg) latest.add(0, new Mov(to, pushReg, sizeHelper.getDefaultSize(), sizeHelper));
        } else {
            latest.add(0, push);
            latest.add(pop);
        }

        if (instructions.size() == 0)  next.addAll(latest);
        else instructions.getLast().addAll(latest);
    }

    @Override
    public void add(final X86Instruction instruction) {
        if(instruction instanceof Pop) {
            reduceIfPosible((Pop) instruction);
        } else if (instruction instanceof Push) {
            final List<X86Instruction> list = new LinkedList<>();
            list.add(instruction);
            instructions.addLast(list);
        } else {
            if (instructions.size() == 0)  next.add(instruction);
            else instructions.getLast().add(instruction);
        }
    }

    @Override
    public void addAll(final X86Instruction[] instructions) {
        for (final X86Instruction instruction : instructions) {
            add(instruction);
        }
    }

    @Override
    public void addAll(final Collection<X86Instruction> instructions) {
        for (final X86Instruction instruction : instructions) {
            add(instruction);
        }
    }

    @Override
    public void addAll(final InstructionsAndTiming<X86Instruction> other) {
        other.addToHolder(this);
    }

    @Override
    public void flush(final PrintStream printer) {
        for(final List<X86Instruction> instructionList : instructions) {
            next.addAll(instructionList);
        }

        instructions.clear();

        next.flush(printer);
    }
}
