package cs444.codegen.tiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;

public class InstructionsAndTiming<T extends Instruction<T>> implements Addable<T>{
    private final List<T> instructions = new LinkedList<T>();
    private long totaltime = 0;
    private long totalSpace = 0;

    @Override
    public void add(final T instruction){
        totaltime += instruction.time;
        totalSpace += instruction.space;
        instructions.add(instruction);
    }

    @Override
    public void addAll(final Collection<T> instructions){
        for(final T instruction : instructions){
            totaltime += instruction.time;
            totalSpace += instruction.space;
            this.instructions.add(instruction);
        }
    }

    @Override
    public void addAll(final InstructionsAndTiming<T> other){
        totaltime += other.totaltime;
        totalSpace += other.totalSpace;
        instructions.addAll(other.instructions);
    }

    public void addToHolder(final InstructionHolder<T> holder){
        holder.addAll(instructions);
    }

    public boolean isBetterThan(final InstructionsAndTiming<T> other){
        return other == null || totaltime < other.totaltime || (totaltime == other.totaltime && totalSpace < other.totalSpace);
    }

    @Override
    public void addAll(final T[] instructions) {
        addAll(Arrays.asList(instructions));
    }
}
