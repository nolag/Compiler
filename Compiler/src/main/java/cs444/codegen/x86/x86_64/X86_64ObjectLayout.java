package cs444.codegen.x86.x86_64;

import cs444.codegen.Addable;
import cs444.codegen.x86.BasicMemoryFormat;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86ObjectLayout;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.types.APkgClassResolver;

public class X86_64ObjectLayout extends X86ObjectLayout {
    public static final X86_64ObjectLayout layout = new X86_64ObjectLayout();
    
    private X86_64ObjectLayout() {
        super(X86SizeHelper.sizeHelper64);
    }
    
    @Override
    public void initialize(final APkgClassResolver typeDclNode, final Addable<X86Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        final Immediate classSITLabel = new Immediate(typeDclNode.generateSIT());
        instructions.add(new Extern(classSITLabel));
        instructions.add(new Mov(Register.COUNTER, classSITLabel, sizeHelper));
        instructions.add(new Mov(new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.ACCUMULATOR)), Register.COUNTER, sizeHelper));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        final Immediate subtypeITLabel = new Immediate(typeDclNode.generateSubtypeIT());
        instructions.add(new Extern(subtypeITLabel));
        instructions.add(new Mov(Register.COUNTER, subtypeITLabel, sizeHelper));
        instructions.add(new Mov(new Memory(format), Register.COUNTER, sizeHelper));
    }
}
