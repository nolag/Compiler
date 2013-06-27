package cs444.codegen;

import java.util.Collection;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;

public interface Addable<T extends Instruction> {

    public void add(T instruction);
    public void addAll(Collection<T> instructions);
    public void addAll(final InstructionsAndTiming<T> other);
}