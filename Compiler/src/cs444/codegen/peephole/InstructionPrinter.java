package cs444.codegen.peephole;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.instructions.Instruction;

public class InstructionPrinter implements InstructionHolder {
    final List<Instruction> instructions = new LinkedList<Instruction>();

    @Override
    public void passToNext(final PrintStream printer) {
        for(final Instruction instruction : instructions) printer.println(instruction.generate());
        instructions.clear();
    }

    @Override
    public void add(final Instruction instruction) {
        instructions.add(instruction);
    }
}
