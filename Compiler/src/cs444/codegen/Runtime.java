package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Call;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Mov;

public class Runtime {
    private static final Immediate MALLOC = new Immediate("__malloc");
    private static final Immediate EXCEPTION = new Immediate("__exception");

    public static void malloc(long bytes, List<Instruction> instructions) {
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(Long.toString(bytes))));
        instructions.add(new Call(MALLOC));
    }

    public static void externAll(List<Instruction> instructions) {
        instructions.add(new Extern(MALLOC));
        instructions.add(new Extern(EXCEPTION));
    }
}
