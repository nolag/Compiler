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
        String className = typeDclNode.fullName;

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        // save eax
        instructions.add(new Push(Register.ACCUMULATOR));

        instructions.add(new Extern(new Immediate(className)));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), new Immediate(className)));

        // TODO: initialize pointer to Subtype checking.

        // TODO: do this in _INIT_FIELDS that every constructor will call
        if (typeDclNode instanceof PkgClassResolver){
            AInterfaceOrClassSymbol classSym = ((PkgClassResolver) typeDclNode).getStart();
            for (DclSymbol field : classSym.getFields()) {
                Size size = SizeHelper.getSizeOfType(field.type.value);
                instructions.add(new Comment("Set field " + field.dclName + " of type " 
                        + field.type.value + " to NULL"));
                instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR, field.getOffset()), 
                        Immediate.NULL, size));
            }
        }

        instructions.add(new Comment("Pop the object address to aex"));
        instructions.add(new Pop(Register.ACCUMULATOR));
        //instructions.add(new Mov(Register.ACCUMULATOR, new PointerRegister(Register.ACCUMULATOR)));
    }

}
