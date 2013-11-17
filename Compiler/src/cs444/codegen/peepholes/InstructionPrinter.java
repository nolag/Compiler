package cs444.codegen.peepholes;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;

public class InstructionPrinter<T extends Instruction> implements InstructionHolder<T>{
    private List<T> instructions = new LinkedList<T>();

    @Override
    public void flush(final Platform<T, ?> platform, final PrintStream printer) {
        for(final T instruction : instructions) printer.println(instruction.generate());
        instructions = new LinkedList<T>();
    }

    @Override
    public void add(final T instruction) {
        instructions.add(instruction);
    }

    @Override
    public void addAll(final Collection<T> instructions) {
        this.instructions.addAll(instructions);
    }

    @Override
    public void addAll(final InstructionsAndTiming<T> other) {
        other.addToHolder(this);
    }

    @Override
    public void addAll(final T[] instructions) {
        this.instructions.addAll(Arrays.asList(instructions));
    }
}
