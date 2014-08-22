package cs444.codegen.arm.arm32;

import cs444.codegen.Addable;
import cs444.codegen.arm.ArmObjectLayout;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Extern;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.types.APkgClassResolver;

public class Arm32ObjectLayout extends ArmObjectLayout {
    public static final Arm32ObjectLayout instance = new Arm32ObjectLayout();

    public Arm32ObjectLayout() {
        super(ArmSizeHelper.sizeHelper32);
    }

    @Override
    public void initialize(APkgClassResolver typeDclNode, Addable<ArmInstruction> instructions) {
        instructions.add(new Comment("Initializing Pointer to SIT Column"));
        final String classSITStr = typeDclNode.generateSIT();
        final ImmediateStr classSITLabel = new ImmediateStr(classSITStr);
        instructions.add(new Extern(classSITLabel));
        instructions.addAll(ArmSizeHelper.putInReg(Register.R1, classSITStr, sizeHelper));
        instructions.add(new Str(Register.R0, Register.R1, sizeHelper));

        instructions.add(new Comment("Initializing Pointer to Subtype Column"));
        final String subtypeITStr = typeDclNode.generateSubtypeIT();
        final ImmediateStr subtypeITLabel = new ImmediateStr(subtypeITStr);
        instructions.add(new Extern(subtypeITLabel));
        instructions.addAll(ArmSizeHelper.putInReg(Register.R1, subtypeITStr, sizeHelper));
        instructions.add(new Str(Register.R0, Register.R1, sizeHelper));
    }
}
