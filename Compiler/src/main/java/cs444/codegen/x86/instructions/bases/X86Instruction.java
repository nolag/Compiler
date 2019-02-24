package cs444.codegen.x86.instructions.bases;

import cs444.codegen.instructions.Instruction;

public abstract class X86Instruction extends Instruction<X86Instruction> {
    protected X86Instruction(int cost, int space) {
        super(cost, space);
    }
}