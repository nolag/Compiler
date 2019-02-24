package cs444.codegen.arm;

import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.SubtypeIndexedTable;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.parser.symbols.ast.TypeSymbol;

import java.util.ArrayList;
import java.util.List;

public abstract class ArmObjectLayout extends ObjectLayout<ArmInstruction, Size> {
    public final ArmSizeHelper sizeHelper;
    private final Immediate12 offset = new Immediate12((short) suptypeOffset);

    protected ArmObjectLayout(ArmSizeHelper sizeHelper) {
        super(sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    @Override
    public List<ArmInstruction> subtypeCheckCode(TypeSymbol subType, Platform<ArmInstruction, Size> platform) {
        List<ArmInstruction> instructions = new ArrayList<>();
        instructions.add(new Comment("Subtype lookup"));
        instructions.add(new Ldr(Register.R0, Register.R0, offset, sizeHelper));
        SubtypeIndexedTable<ArmInstruction, Size> subTypeTable = platform.getSubtypeTable();
        Immediate12 offset = new Immediate12((short) subTypeTable.getOffset(subType.getTypeDclNode().fullName));

        instructions.add(new Ldr(sizeHelper.getCellSize(), Condition.AL, Register.R0, Register.R0, offset, sizeHelper));

        return instructions;
    }
}
