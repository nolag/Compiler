package cs444.codegen.peepholes;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;

import java.util.Collection;

public abstract class BasicInstrucitonHolder<T extends Instruction<T>> implements InstructionHolder<T> {
    @Override
    public final void addAll(T[] instructions) {
        for (T instruction : instructions) {
            add(instruction);
        }
    }

    @Override
    public final void addAll(Collection<T> instructions) {
        for (T instruction : instructions) {
            add(instruction);
        }
    }

    @Override
    public final void addAll(InstructionsAndTiming<T> other) {
        other.addToHolder(this);
    }
}
