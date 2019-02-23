package cs444.codegen.peepholes;

import java.util.Collection;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;

public abstract class BasicInstrucitonHolder<T extends Instruction<T>> implements InstructionHolder<T> {
    @Override
    public final void addAll(final T[] instructions) {
        for (final T instruction : instructions) add(instruction);
    }

    @Override
    public final void addAll(final Collection<T> instructions) {
        for (final T instruction : instructions) add(instruction);
    }

    @Override
    public final void addAll(final InstructionsAndTiming<T> other) {
        other.addToHolder(this);
    }
}
