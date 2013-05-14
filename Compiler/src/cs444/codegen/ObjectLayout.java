package cs444.codegen;

import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Movzx;
import cs444.codegen.peephole.InstructionHolder;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public class ObjectLayout {
    public final static int SUBTYPE_OFFSET = SizeHelper.DEFAULT_STACK_SIZE;

    public static void initialize(final APkgClassResolver typeDclNode, final InstructionHolder instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        final Immediate classSITLabel = new Immediate(typeDclNode.generateSIT());
        instructions.add(new Extern(classSITLabel));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), classSITLabel));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        final Immediate subtypeITLabel = new Immediate(typeDclNode.generateSubtypeIT());
        instructions.add(new Extern(subtypeITLabel));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR, SUBTYPE_OFFSET), subtypeITLabel));
    }

    public static void subtypeCheckCode(final TypeSymbol subType,
            final SubtypeIndexedTable subtypeITable, final InstructionHolder instructions) {

        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Mov(Register.ACCUMULATOR, new PointerRegister(Register.ACCUMULATOR, ObjectLayout.SUBTYPE_OFFSET)));
        final PointerRegister instanceOfInfo = new PointerRegister(Register.ACCUMULATOR, subtypeITable.getOffset(subType.getTypeDclNode().fullName));
        instructions.add(new Movzx(Register.ACCUMULATOR, instanceOfInfo, subtypeITable.dataSize));
    }

    public static long objSize(){
        return SizeHelper.DEFAULT_STACK_SIZE  * 2;
    }
}
