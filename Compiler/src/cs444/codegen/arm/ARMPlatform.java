package cs444.codegen.arm;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cs444.codegen.Addable;
import cs444.codegen.IRuntime;
import cs444.codegen.OperatingSystem;
import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Extern;
import cs444.codegen.arm.instructions.Global;
import cs444.codegen.arm.instructions.Label;
import cs444.codegen.arm.instructions.Section;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public abstract class ArmPlatform extends Platform<ArmInstruction, Size> {
    public interface ARMPlatformFactory<P extends ArmPlatform> extends PlatformFactory<ArmInstruction, Size, P> {
        @Override
        P getPlatform(Set<String> opts);
    }

    private static InstructionHolder<ArmInstruction> genInstructionHolder(final Set<String> options, final ArmSizeHelper sizeHelper) {
        return new InstructionPrinter<>();
    }

    protected final ArmSizeHelper sizeHelper;

    protected ArmPlatform(Set<String> options, String name, IRuntime<ArmInstruction> runtime, TileInit<ArmInstruction, Size> tiles,
            ArmSizeHelper sizeHelper) {
        super(options, name, runtime, tiles, genInstructionHolder(options, sizeHelper), sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    @Override
    public ArmSizeHelper getSizeHelper() {
        return sizeHelper;
    }

    @Override
    public void generateStaticCode(List<APkgClassResolver> resolvers, boolean outputFile, String directory) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public OperatingSystem<? extends Platform<ArmInstruction, Size>>[] getOperatingSystems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void genStartInstructions(String methodName, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void genInstructorInvoke(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void genHeaderStart(Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void genHeaderEnd(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public TileSet<ArmInstruction, Size> getTiles() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void genLayoutForStaticFields(Iterable<DclSymbol> staticFields, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public void zeroStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getNullStr() {
        return Immediate16.NULL.toString();
    }

    @Override
    public String getFalseStr() {
        return Immediate16.FALSE.toString();
    }

    @Override
    public String getTrueStr() {
        return Immediate16.TRUE.toString();
    }

    @Override
    public ArmInstruction comment(final String value) {
        return new Comment(value);
    }

    @Override
    public ArmInstruction getExtern(final String what) {
        return new Extern(what);
    }

    @Override
    public Label getLabel(final String what) {
        return new Label(what);
    }

    @Override
    public Global getGlobal(final String what) {
        return new Global(what);
    }

    @Override
    public Section getDataSection() {
        return Section.DATA;
    }
}
