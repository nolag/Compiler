package cs444.codegen.tiles;

import java.util.LinkedList;
import java.util.List;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;

public class InstructionsAndTiming<T extends Instruction>{
    private final List<T> instructions = new LinkedList<T>();
    private double totalCost = 0;

    public void add(final T instruction){
        totalCost += instruction.cost;
        instructions.add(instruction);
    }

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
}
