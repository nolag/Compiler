package cs444.codegen.x86;

import java.util.LinkedList;
import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Movzx;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public class X86ObjectLayout implements ObjectLayout<X86Instruction> {
    public static final X86ObjectLayout object32 = new X86ObjectLayout(false);
    public static final X86ObjectLayout object64 = new X86ObjectLayout(true);

    public final int SUBTYPE_OFFSET;
    public final X86SizeHelper sizeHelper;

    private X86ObjectLayout(final boolean use64){
        sizeHelper = use64 ? X86SizeHelper.sizeHelper64 : X86SizeHelper.sizeHelper32;
        SUBTYPE_OFFSET = sizeHelper.getDefaultStackSize();
    }

    @Override
    public void initialize(final APkgClassResolver typeDclNode, final Addable<X86Instruction> instructions) {

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
    public List<X86Instruction> subtypeCheckCode(final TypeSymbol subType, final Platform<X86Instruction, ?> platform) {
        final List<X86Instruction> instructions = new LinkedList<X86Instruction>();
        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Mov(Register.ACCUMULATOR, new Memory(Register.ACCUMULATOR, SUBTYPE_OFFSET), sizeHelper));
        final long offset = platform.getSubtypeTable().getOffset(subType.getTypeDclNode().fullName);
        final Memory instanceOfInfo = new Memory(Register.ACCUMULATOR, offset);
        instructions.add(new Movzx(Register.ACCUMULATOR, instanceOfInfo, SubtypeCellGen.dataSize, sizeHelper));
        return instructions;
    }

    @Override
    public long objSize(){
        return SUBTYPE_OFFSET  * 2;
    }
}
