package cs444.codegen.arm;

import java.util.List;

import cs444.codegen.Addable;
import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public class ArmObjectLayout extends ObjectLayout<ArmInstruction, Size> {
    public final ArmSizeHelper sizeHelper;

    protected ArmObjectLayout(ArmSizeHelper sizeHelper) {
        super(sizeHelper);
        this.sizeHelper = sizeHelper;
    }
    
    @Override
    public void initialize(APkgClassResolver typeDclNode, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ArmInstruction> subtypeCheckCode(TypeSymbol subType, Platform<ArmInstruction, Size> platform) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long objSize() {
        // TODO Auto-generated method stub
        return 0;
    }

}
