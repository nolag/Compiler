package cs444.codegen.x86;

import cs444.codegen.IPlatform;
import cs444.codegen.ObjectLayout;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Movzx;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public class X86ObjectLayout implements ObjectLayout<X86Instruction> {
    public static final X86ObjectLayout object32 = new X86ObjectLayout(false);
    public static final X86ObjectLayout object64 = new X86ObjectLayout(false);

    public final int SUBTYPE_OFFSET;
    public final X86SizeHelper sizeHelper;

    private X86ObjectLayout(final boolean use64){
        sizeHelper = use64 ? X86SizeHelper.sizeHelper64 : X86SizeHelper.sizeHelper32;
        SUBTYPE_OFFSET = sizeHelper.defaultStackSize;
    }

    @Override
    public void initialize(final APkgClassResolver typeDclNode, final InstructionHolder<X86Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        final Immediate classSITLabel = new Immediate(typeDclNode.generateSIT());
        instructions.add(new Extern(classSITLabel));
        instructions.add(new Mov(new Memory(Register.ACCUMULATOR), classSITLabel, sizeHelper));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        final Immediate subtypeITLabel = new Immediate(typeDclNode.generateSubtypeIT());
        instructions.add(new Extern(subtypeITLabel));
        instructions.add(new Mov(new Memory(Register.ACCUMULATOR, SUBTYPE_OFFSET), subtypeITLabel, sizeHelper));
    }

    @Override
    public void subtypeCheckCode(final TypeSymbol subType, final IPlatform<X86Instruction> platform) {
        final InstructionHolder<X86Instruction> instructions = platform.getInstructionHolder();
        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Mov(Register.ACCUMULATOR, new Memory(Register.ACCUMULATOR, SUBTYPE_OFFSET), sizeHelper));
        final long offset = platform.getSubtypeTable().getOffset(subType.getTypeDclNode().fullName);
        final Memory instanceOfInfo = new Memory(Register.ACCUMULATOR, offset);
        instructions.add(new Movzx(Register.ACCUMULATOR, instanceOfInfo, SubtypeCellGen.dataSize, sizeHelper));
    }

    @Override
    public long objSize(){
        return SUBTYPE_OFFSET  * 2;
    }
}
