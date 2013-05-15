package cs444.codegen.x86;

import cs444.codegen.ObjectLayout;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Movzx;
import cs444.codegen.instructions.x86.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public class X86ObjectLayout extends ObjectLayout<X86Instruction> {
    public static final int SUBTYPE_OFFSET = SizeHelper.DEFAULT_STACK_SIZE;
    public static final X86ObjectLayout instance = new X86ObjectLayout();


    private X86ObjectLayout(){ }

    @Override
    public void initialize(final APkgClassResolver typeDclNode, final InstructionHolder<X86Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        final Immediate classSITLabel = new Immediate(typeDclNode.generateSIT());
        instructions.add(new Extern(classSITLabel));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR), classSITLabel));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        final Immediate subtypeITLabel = new Immediate(typeDclNode.generateSubtypeIT());
        instructions.add(new Extern(subtypeITLabel));
        instructions.add(new Mov(new PointerRegister(Register.ACCUMULATOR, SUBTYPE_OFFSET), subtypeITLabel));
    }

    @Override
    public void subtypeCheckCode(final TypeSymbol subType, final SubtypeIndexedTable subtypeITable,
            final InstructionHolder<X86Instruction> instructions) {

        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Mov(Register.ACCUMULATOR, new PointerRegister(Register.ACCUMULATOR, X86ObjectLayout.SUBTYPE_OFFSET)));
        final PointerRegister instanceOfInfo = new PointerRegister(Register.ACCUMULATOR, subtypeITable.getOffset(subType.getTypeDclNode().fullName));
        instructions.add(new Movzx(Register.ACCUMULATOR, instanceOfInfo, subtypeITable.dataSize));
    }

    public static long objSize(){
        return SizeHelper.DEFAULT_STACK_SIZE  * 2;
    }
}
