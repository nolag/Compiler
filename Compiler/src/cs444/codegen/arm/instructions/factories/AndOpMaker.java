package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.And;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class AndOpMaker implements BinOpMaker {
    public static AndOpMaker maker = new AndOpMaker();
    
    private AndOpMaker() { }
    

    @Override
    public And make(Register dest, Register lhs, Operand2 rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new And(dest, lhs, rhs, sizeHelper);
    }
}
