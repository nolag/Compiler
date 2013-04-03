package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Mov;
import cs444.types.APkgClassResolver;

public class ObjectLayout {
    public final static int SUBTYPE_OFFSET = SizeHelper.DEFAULT_STACK_SIZE;

    public static void initialize(APkgClassResolver typeDclNode,
            List<Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        Immediate classSITLabel = new Immediate(typeDclNode.generateSIT());
        instructions.add(new Extern(classSITLabel));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), classSITLabel));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        Immediate subtypeITLabel = new Immediate(typeDclNode.generateSubtypeIT());
        instructions.add(new Extern(subtypeITLabel));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR, SUBTYPE_OFFSET),
                subtypeITLabel));
    }

}
