package cs444.codegen.x86;

import java.util.LinkedList;
import java.util.List;

import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Movzx;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.TypeSymbol;

public abstract class X86ObjectLayout implements ObjectLayout<X86Instruction> {
    public final int SUBTYPE_OFFSET;
    public final X86SizeHelper sizeHelper;
    protected final MemoryFormat format;

    protected X86ObjectLayout(X86SizeHelper sizeHelper){
        this.sizeHelper = sizeHelper;
        SUBTYPE_OFFSET = sizeHelper.getDefaultStackSize();
        format = new AddMemoryFormat(Register.ACCUMULATOR, new Immediate(SUBTYPE_OFFSET));
    }

    @Override
    public List<X86Instruction> subtypeCheckCode(final TypeSymbol subType, final Platform<X86Instruction, ?> platform) {
        final List<X86Instruction> instructions = new LinkedList<X86Instruction>();
        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Mov(Register.ACCUMULATOR, new Memory(format), sizeHelper));
        final Immediate offset = new Immediate(platform.getSubtypeTable().getOffset(subType.getTypeDclNode().fullName));
        final Memory instanceOfInfo = new Memory(new AddMemoryFormat(Register.ACCUMULATOR, offset));
        instructions.add(new Movzx(Register.ACCUMULATOR, instanceOfInfo, SubtypeCellGen.dataSize, sizeHelper));
        return instructions;
    }

    @Override
    public long objSize(){
        return SUBTYPE_OFFSET  * 2;
    }
}
