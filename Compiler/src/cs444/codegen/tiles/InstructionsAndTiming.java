package cs444.codegen.tiles;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;

public class InstructionsAndTiming<T extends Instruction> implements Addable<T>{
    private final List<T> instructions = new LinkedList<T>();
    private double totalCost = 0;

    @Override
    public void add(final T instruction){
        totalCost += instruction.cost;
        instructions.add(instruction);
    }

    @Override
    public void addAll(final Collection<T> instructions){
        for(final T instruction : instructions){
            totalCost += instruction.cost;
            this.instructions.add(instruction);
        }
    }

    @Override
    public void addAll(final InstructionsAndTiming<T> other){
        totalCost += other.totalCost;
        instructions.addAll(other.instructions);
    }

    public void addToHolder(final InstructionHolder<T> holder){
        holder.addAll(instructions);
    }

    public boolean isBetterThan(final InstructionsAndTiming<T> other){
        return other == null || totalCost < other.totalCost;
    }

    @Override
    public void addAll(final T[] instructions) {
        addAll(Arrays.asList(instructions));
    }
}
