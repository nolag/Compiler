package cs444.codegen.x86.peepholes;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.peepholes.BasicInstrucitonHolder;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class PushPopRemover extends BasicInstrucitonHolder<X86Instruction> {
    private final Deque<List<X86Instruction>> instructions = new ArrayDeque<>();
    private final InstructionHolder<X86Instruction> next;
    private final SizeHelper<X86Instruction, Size> sizeHelper;

    public PushPopRemover(InstructionHolder<X86Instruction> next,
                          SizeHelper<X86Instruction, Size> sizeHelper) {
        this.next = next;
        this.sizeHelper = sizeHelper;
    }

    private void reduceIfPosible(Pop pop) {
        Register to = (Register) pop.data.get(0);
        List<X86Instruction> latest = instructions.removeLast();
        Push push = (Push) latest.remove(0);
        Register pushReg = (Register) push.data.get(0);

        boolean usedReg = false;
        for (X86Instruction instruction : latest) {
            if (instruction.uses(to)) {
                usedReg = true;
                break;
            }
        }

        if (!usedReg) {
            if (to != pushReg) {
                latest.add(0, new Mov(to, pushReg, sizeHelper.getDefaultSize(), sizeHelper));
            }
        } else {
            latest.add(0, push);
            latest.add(pop);
        }

        if (instructions.size() == 0) {
            next.addAll(latest);
        } else {
            instructions.getLast().addAll(latest);
        }
    }

    @Override
    public void add(X86Instruction instruction) {
        if (instruction instanceof Pop) {
            reduceIfPosible((Pop) instruction);
        } else if (instruction instanceof Push) {
            List<X86Instruction> list = new LinkedList<>();
            list.add(instruction);
            instructions.addLast(list);
        } else {
            if (instructions.size() == 0) {
                next.add(instruction);
            } else {
                instructions.getLast().add(instruction);
            }
        }
    }

    @Override
    public void flush(Platform<X86Instruction, ?> platform, PrintStream printer) {
        for (List<X86Instruction> instructionList : instructions) {
            next.addAll(instructionList);
        }

        instructions.clear();

        next.flush(platform, printer);
    }
}
