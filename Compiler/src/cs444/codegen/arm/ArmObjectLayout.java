package cs444.codegen.arm;

import cs444.codegen.Addable;
import cs444.codegen.ObjectLayout;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.types.APkgClassResolver;

public abstract class ArmObjectLayout extends ObjectLayout<ArmInstruction, Size> {
    public final ArmSizeHelper sizeHelper;

    protected ArmObjectLayout(ArmSizeHelper sizeHelper) {
        super(sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    @Override
    public void initialize(APkgClassResolver typeDclNode, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }
}
