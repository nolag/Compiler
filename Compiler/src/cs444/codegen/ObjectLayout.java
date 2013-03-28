package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Pop;
import cs444.codegen.instructions.Push;
import cs444.types.APkgClassResolver;

public class ObjectLayout {
    private final static int SUBTYPE_OFFSET = SizeHelper.DEFAULT_STACK_SIZE;

    public static void initialize(APkgClassResolver typeDclNode,
            List<Instruction> instructions) {
        String className = typeDclNode.fullName;

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        // save eax
        instructions.add(new Push(Register.ACCUMULATOR));

        instructions.add(new Extern(new Immediate(className)));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), new Immediate(className)));

        // TODO: initialize pointer to Subtype checking.
        // TODO: we probably want to assign 0 to all fields of object

        instructions.add(new Comment("Pop the object address to aex"));
        instructions.add(new Pop(Register.ACCUMULATOR));
        //instructions.add(new Mov(Register.ACCUMULATOR, new PointerRegister(Register.ACCUMULATOR)));
    }

}
