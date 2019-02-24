package cs444.codegen.x86.x86_32;

import cs444.codegen.Addable;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.types.APkgClassResolver;

public class X86_32ObjectLayout extends X86ObjectLayout {
    public static final X86_32ObjectLayout layout = new X86_32ObjectLayout();

    private X86_32ObjectLayout() {
        super(X86SizeHelper.sizeHelper32);
    }

    @Override
    public void initialize(APkgClassResolver typeDclNode, Addable<X86Instruction> instructions) {

        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        Immediate classSITLabel = new Immediate(typeDclNode.generateSIT());
        instructions.add(new Extern(classSITLabel));
        instructions.add(new Mov(new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.ACCUMULATOR)),
                classSITLabel, sizeHelper));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        Immediate subtypeITLabel = new Immediate(typeDclNode.generateSubtypeIT());
        instructions.add(new Extern(subtypeITLabel));
        instructions.add(new Mov(new Memory(format), subtypeITLabel, sizeHelper));
    }
}
