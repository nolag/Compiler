package cs444.codegen.peepholes;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;

public class InstructionPrinter<T extends Instruction<T>> extends BasicInstrucitonHolder<T> {
    private List<T> instructions = new LinkedList<T>();

    @Override
    public void flush(final Platform<T, ?> platform, final PrintStream printer) {
        for (final T instruction : instructions) {
            printer.println(instruction.generate());
        }
        instructions = new LinkedList<T>();
    }

    @Override
    public void add(final T instruction) {
        instructions.add(instruction);
    }
}
