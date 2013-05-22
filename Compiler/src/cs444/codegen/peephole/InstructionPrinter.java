package cs444.codegen.peephole;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.instructions.Instruction;

public class InstructionPrinter<T extends Instruction> implements InstructionHolder<T> {
    private List<T> instructions = new LinkedList<T>();

    @Override
    public void flush(final PrintStream printer) {
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
}
