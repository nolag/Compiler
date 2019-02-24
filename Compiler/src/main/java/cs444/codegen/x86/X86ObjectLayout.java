package cs444.codegen.x86;

import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Movzx;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.TypeSymbol;

import java.util.ArrayList;
import java.util.List;

public abstract class X86ObjectLayout extends ObjectLayout<X86Instruction, Size> {
    public final X86SizeHelper sizeHelper;
    protected final MemoryFormat format;

    protected X86ObjectLayout(X86SizeHelper sizeHelper) {
        super(sizeHelper);
        this.sizeHelper = sizeHelper;
        format = new AddMemoryFormat(Register.ACCUMULATOR, new Immediate(suptypeOffset));
    }

    @Override
    public List<X86Instruction> subtypeCheckCode(TypeSymbol subType,
                                                 Platform<X86Instruction, Size> platform) {
        List<X86Instruction> instructions = new ArrayList<X86Instruction>();
        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Mov(Register.ACCUMULATOR, new Memory(format), sizeHelper));
        SubtypeIndexedTable<X86Instruction, Size> subTypeTable = platform.getSubtypeTable();
        Immediate offset = new Immediate(subTypeTable.getOffset(subType.getTypeDclNode().fullName));
        Memory instanceOfInfo = new Memory(new AddMemoryFormat(Register.ACCUMULATOR, offset));
        instructions.add(new Movzx(Register.ACCUMULATOR, instanceOfInfo, sizeHelper.getCellSize(), sizeHelper));
        return instructions;
    }
}
