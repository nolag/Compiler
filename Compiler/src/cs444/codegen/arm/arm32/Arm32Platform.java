package cs444.codegen.arm.arm32;

import java.util.Set;

import cs444.codegen.Addable;
import cs444.codegen.ObjectLayout;
import cs444.codegen.OperatingSystem;
import cs444.codegen.Platform;
import cs444.codegen.arm.ArmPlatform;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.types.APkgClassResolver;

public class Arm32Platform extends ArmPlatform {

    protected Arm32Platform(Set<String> options) {
        //TODO
        super(options, "a32", null, null, null);
    }

    @Override
    public ObjectLayout<ArmInstruction, Size> getObjectLayout() {
        return Arm32ObjectLayout.instance;
    }

    @Override
    public OperatingSystem<? extends Platform<ArmInstruction, Size>>[] getOperatingSystems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void genInstructorInvoke(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void genHeaderEnd(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public TileSet<ArmInstruction, Size> getTiles() {
        return TileSet.<ArmInstruction, Size> getOrMake(Arm32Platform.class);
    }

    @Override
    public TileHelper<ArmInstruction, Size> getTileHelper() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void zeroDefaultLocation(Addable<ArmInstruction> instructions) {
        instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
    }

    @Override
    public void moveStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        instructions.addAll(ArmSizeHelper.storeStatic(Register.R0, staticLbl, sizeHelper));
    }

    @Override
    public void zeroStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
        instructions.addAll(ArmSizeHelper.storeStatic(Register.R0, staticLbl, sizeHelper));
    }

    @Override
    public void moveStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }
}
