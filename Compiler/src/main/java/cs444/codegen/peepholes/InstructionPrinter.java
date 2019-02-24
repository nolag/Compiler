package cs444.codegen.peepholes;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class InstructionPrinter<T extends Instruction<T>> extends BasicInstrucitonHolder<T> {
    private List<T> instructions = new LinkedList<T>();

    @Override
    public void flush(Platform<T, ?> platform, PrintStream printer) {
        for (T instruction : instructions) {
            printer.println(instruction.generate());
        }
        instructions = new LinkedList<T>();
    }

    @Override
    public void add(T instruction) {
        instructions.add(instruction);
    }
}
