package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Mov;
import cs444.types.APkgClassResolver;

public class ObjectLayout {
    private final static int SUBTYPE_OFFSET = SizeHelper.DEFAULT_STACK_SIZE;

    public static void initialize(APkgClassResolver typeDclNode,
            List<Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));

        String classNameSIT = typeDclNode.generateSIT();
        instructions.add(new Extern(new Immediate(classNameSIT)));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), new Immediate(classNameSIT)));

        // TODO: initialize pointer to Subtype checking.

        // TODO: According to J1_A_FieldInitialization_Before, we need to preinitialize all fields to 0

        //instructions.add(new Mov(Register.ACCUMULATOR, new PointerRegister(Register.ACCUMULATOR)));
    }

}
