package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;

import java.util.Collection;

public interface Addable<T extends Instruction<T>> {
    void add(T instruction);

    void addAll(T[] instructions);

    void addAll(Collection<T> instructions);

    void addAll(InstructionsAndTiming<T> other);
}