package cs444.x86;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.peepholes.MovZeroRegRemover;
import cs444.codegen.x86.peepholes.PushPopRemover;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PeepholeTests {
    @Test
    public void basicPushPopRemover() {
        MockInstrucionHolder mock = new MockInstrucionHolder();
        SizeHelper<X86Instruction, Size> sizeHelper = X86SizeHelper.sizeHelper32;
        InstructionHolder<X86Instruction> holder = new PushPopRemover(mock, sizeHelper);
        holder.add(new Push(Register.ACCUMULATOR, sizeHelper));
        holder.add(new Pop(Register.ACCUMULATOR, sizeHelper));
        holder.flush(null, null);
        assertTrue(mock.instructions.size() == 0);
    }

    @Test
    public void basicPushPopReplace() {
        MockInstrucionHolder mock = new MockInstrucionHolder();
        SizeHelper<X86Instruction, Size> sizeHelper = X86SizeHelper.sizeHelper32;
        InstructionHolder<X86Instruction> holder = new PushPopRemover(mock, sizeHelper);
        holder.add(new Push(Register.ACCUMULATOR, sizeHelper));
        holder.add(new Pop(Register.COUNTER, sizeHelper));
        holder.flush(null, null);
        assertTrue(mock.instructions.size() == 1);
        Mov mov = (Mov) mock.instructions.get(0);
        assertEquals(Register.ACCUMULATOR, mov.src);
        assertEquals(Register.COUNTER, mov.dest);
    }

    @Test
    public void complexPushPopReplace() {
        MockInstrucionHolder mock = new MockInstrucionHolder();
        SizeHelper<X86Instruction, Size> sizeHelper = X86SizeHelper.sizeHelper32;
        InstructionHolder<X86Instruction> holder = new PushPopRemover(mock, sizeHelper);
        holder.add(new Push(Register.ACCUMULATOR, sizeHelper));
        holder.add(new Mov(Register.DATA, Register.SOURCE, sizeHelper));
        holder.add(new Push(Register.SOURCE, sizeHelper));
        holder.add(new Mov(Register.DESTINATION, Register.SOURCE, sizeHelper));
        holder.add(new Pop(Register.DATA, sizeHelper));
        holder.add(new Pop(Register.COUNTER, sizeHelper));
        holder.flush(null, null);

        assertEquals(4, mock.instructions.size());

        Mov mov = (Mov) mock.instructions.get(0);
        assertEquals(Register.ACCUMULATOR, mov.src);
        assertEquals(Register.COUNTER, mov.dest);

        mov = (Mov) mock.instructions.get(2);
        assertEquals(Register.SOURCE, mov.src);
        assertEquals(Register.DATA, mov.dest);
    }

    @Test
    public void onePushPopReplace() {
        MockInstrucionHolder mock = new MockInstrucionHolder();
        SizeHelper<X86Instruction, Size> sizeHelper = X86SizeHelper.sizeHelper32;
        InstructionHolder<X86Instruction> holder = new PushPopRemover(mock, sizeHelper);
        holder.add(new Push(Register.ACCUMULATOR, sizeHelper));
        holder.add(new Mov(Register.COUNTER, Register.DATA, sizeHelper));
        holder.add(new Push(Register.SOURCE, sizeHelper));
        holder.add(new Mov(Register.DESTINATION, Register.SOURCE, sizeHelper));
        holder.add(new Pop(Register.DATA, sizeHelper));
        holder.add(new Pop(Register.COUNTER, sizeHelper));
        holder.flush(null, null);

        assertEquals(5, mock.instructions.size());
        Mov mov = (Mov) mock.instructions.get(2);
        assertEquals(Register.SOURCE, mov.src);
        assertEquals(Register.DATA, mov.dest);
    }

    @Test
    public void movZeroRemoved() {
        MockInstrucionHolder mock = new MockInstrucionHolder();
        MovZeroRegRemover holder = new MovZeroRegRemover(mock);
        SizeHelper<X86Instruction, Size> sizeHelper = X86SizeHelper.sizeHelper32;
        X86Instruction mov1 = new Mov(new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.ACCUMULATOR)),
                Immediate.ZERO, sizeHelper);
        holder.add(mov1);
        X86Instruction mov2 = new Mov(Register.ACCUMULATOR, Immediate.ZERO, sizeHelper);
        holder.add(mov2);
        X86Instruction mov3 = new Mov(Register.ACCUMULATOR, Immediate.ONE, sizeHelper);
        holder.add(mov3);

        assertEquals(3, mock.instructions.size());
        assertEquals(mov1, mock.instructions.get(0));
        Xor xor = (Xor) mock.instructions.get(1);
        assertEquals(Register.ACCUMULATOR, xor.arg1);
        assertEquals(Register.ACCUMULATOR, xor.arg2);
        assertEquals(mov3, mock.instructions.get(2));
    }

    public static class MockInstrucionHolder implements InstructionHolder<X86Instruction> {
        public final List<X86Instruction> instructions = new LinkedList<>();

        @Override
        public void add(X86Instruction instruction) {
            instructions.add(instruction);
        }

        @Override
        public void addAll(X86Instruction[] instructions) {
            this.instructions.addAll(Arrays.asList(instructions));
        }

        @Override
        public void addAll(Collection<X86Instruction> instructions) {
            this.instructions.addAll(instructions);
        }

        @Override
        public void addAll(InstructionsAndTiming<X86Instruction> other) {
            other.addToHolder(this);
        }

        @Override
        public void flush(Platform<X86Instruction, ?> platform, PrintStream printer) { }
    }
}
