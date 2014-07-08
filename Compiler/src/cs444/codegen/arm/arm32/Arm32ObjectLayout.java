package cs444.codegen.arm.arm32;

import java.util.List;

import cs444.codegen.Platform;
import cs444.codegen.arm.ArmObjectLayout;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.parser.symbols.ast.TypeSymbol;

public class Arm32ObjectLayout extends ArmObjectLayout {

    public Arm32ObjectLayout() {
        //TODO
        super(null);
    }

    @Override
    public List<ArmInstruction> subtypeCheckCode(TypeSymbol subType, Platform<ArmInstruction, Size> platform) {
        // TODO Auto-generated method stub
        return null;
    }
}
