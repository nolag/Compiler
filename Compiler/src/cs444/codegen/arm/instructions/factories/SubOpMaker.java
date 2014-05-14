package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.Sub;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class SubOpMaker implements BinOpMaker {
    public static SubOpMaker maker = new SubOpMaker();
    
    private SubOpMaker() { }
    

    @Override
    public Sub make(Register dest, Register lhs, Operand2 rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Sub(dest, lhs, rhs, sizeHelper);
    }
}
