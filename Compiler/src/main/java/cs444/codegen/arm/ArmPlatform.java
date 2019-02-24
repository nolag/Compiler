package cs444.codegen.arm;

import cs444.codegen.Addable;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;

import java.util.Set;

public abstract class ArmPlatform extends Platform<ArmInstruction, Size> {
    protected final ArmSizeHelper sizeHelper;

    protected ArmPlatform(Set<String> options, String name, IRuntime<ArmInstruction> runtime, TileInit<ArmInstruction
            , Size> tiles,
                          ArmSizeHelper sizeHelper) {
        super(options, name, runtime, tiles, genInstructionHolder(options, sizeHelper), sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    private static InstructionHolder<ArmInstruction> genInstructionHolder(Set<String> options,
                                                                          ArmSizeHelper sizeHelper) {
        return new InstructionPrinter<>();
    }

    @Override
    public void genHeaderStart(Addable<ArmInstruction> instructions) {
        super.genHeaderStart(instructions);
        instructions.add(ArmTileHelper.ENTER);
    }

    @Override
    public void getEnterStaticField(Addable<ArmInstruction> instructions) {
        super.getEnterStaticField(instructions);
        instructions.add(ArmTileHelper.ENTER);
    }

    @Override
    public String getNullStr() {
        return String.valueOf(Immediate8.NULL.value);
    }

    @Override
    public String getFalseStr() {
        return String.valueOf(Immediate8.FALSE.value);
    }

    @Override
    public String getTrueStr() {
        return String.valueOf(Immediate8.TRUE.value);
    }

    @Override
    public ArmInstruction makeComment(String value) {
        return new Comment(value);
    }

    @Override
    public ArmInstruction makeExtern(String what) {
        return new Extern(new ImmediateStr(what));
    }

    @Override
    public Label makeLabel(String what) {
        return new Label(what);
    }

    @Override
    public Global makeGlobal(String what) {
        return new Global(what);
    }

    @Override
    public Section getDataSection() {
        return Section.DATA;
    }

    @Override
    public Section getTextSection() {
        return Section.TEXT;
    }

    @Override
    public Section getBSSSection() {
        return Section.BSS;
    }

    @Override
    public Bl makeCall(String what) {
        return new Bl(what);
    }

    @Override
    public ArmInstruction getRetStaticField() {
        return ArmTileHelper.LEAVE;
    }

    @Override
    public ArmInstruction[] makeSpace(String name, Size size) {
        if (size == Size.D) {
            return new ArmInstruction[]{new Label(name), Word.zeroWord, Word.zeroWord};
        }
        return new ArmInstruction[]{new Label(name), Word.zeroWord};
    }

    public interface ArmPlatformFactory<P extends ArmPlatform> extends PlatformFactory<ArmInstruction, Size, P> {
        @Override
        P getPlatform(Set<String> opts);
    }
}
