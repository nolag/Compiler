package cs444.codegen;

import java.util.List;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Pop;
import cs444.codegen.instructions.Push;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassResolver;

public class ObjectLayout {
    private final static int SUBTYPE_OFFSET = SizeHelper.DEFAULT_STACK_SIZE;

    public static void initialize(APkgClassResolver typeDclNode,
            List<Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));

        String classNameSIT = typeDclNode.generateSIT();
        instructions.add(new Extern(new Immediate(classNameSIT)));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), new Immediate(classNameSIT)));

        // TODO: initialize pointer to Subtype checking.

        //instructions.add(new Mov(Register.ACCUMULATOR, new PointerRegister(Register.ACCUMULATOR)));
    }

}
