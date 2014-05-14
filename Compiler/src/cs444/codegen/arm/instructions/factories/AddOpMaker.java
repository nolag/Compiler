package cs444.codegen.arm.instructions.factories;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.bases.ArmInstruction;

public class AddOpMaker implements BinOpMaker {
    public static AddOpMaker maker = new AddOpMaker();
    
    private AddOpMaker() { }
    

    @Override
    public Add make(Register dest, Register lhs, Operand2 rhs, SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Add(dest, lhs, rhs, sizeHelper);
    }
}
