package cs444.codegen.arm.arm32;

import cs444.codegen.*;
import cs444.codegen.arm.*;
import cs444.codegen.arm.arm32.tiles.helpers.Arm32TileHelper;
import cs444.codegen.arm.arm32.tiles.helpers.Arm32TileInit;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import utils.GenericMaker;

import java.util.Collections;
import java.util.Set;

public class Arm32Platform extends ArmPlatform {

    private final OperatingSystem<Arm32Platform>[] oses =
            GenericMaker.<OperatingSystem<Arm32Platform>>makeArray(new Linux(this));

    protected Arm32Platform(Set<String> options) {
        super(options, "arm", Runtime.instance, Arm32TileInit.instance, ArmSizeHelper.sizeHelper32);
    }

    @Override
    public ObjectLayout<ArmInstruction, Size> getObjectLayout() {
        return Arm32ObjectLayout.instance;
    }

    @Override
    public OperatingSystem<? extends Platform<ArmInstruction, Size>>[] getOperatingSystems() {
        return oses;
    }

    @Override
    public void genInstructorInvoke(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        Arm32TileHelper.instance.invokeConstructor(resolver, Collections.emptyList(), this, instructions);
    }

    @Override
    public void genHeaderEnd(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        instructions.add(new Push(Register.R11));
        instructions.add(new Comment("Store pointer to object in R11"));
        instructions.add(new Mov(Register.R11, Register.R0, sizeHelper));

        for (DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            long offset = fieldDcl.getOffset(this);

            if (!fieldDcl.children.isEmpty()) {
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));

                CodeGenVisitor<ArmInstruction, Size> visitor = new CodeGenVisitor<ArmInstruction, Size>(
                        CodeGenVisitor.getCurrentCodeGen(this).currentFile, this);

                ISymbol field = fieldDcl.children.get(0);
                field.accept(visitor);
                instructions.addAll(getBest(field));

                if (fieldDcl.getType().value.equals(JoosNonTerminal.LONG)) {
                    instructions.add(new Str(Register.R2, Register.R11, new Immediate12((short) (offset + 4)),
                            sizeHelper));
                    instructions.add(new Str(Register.R0, Register.R11, new Immediate12((short) offset), sizeHelper));
                } else {
                    instructions.add(new Str(Register.R0, Register.R11, new Immediate12((short) offset), sizeHelper));
                }
            }
        }

        instructions.add(new Pop(Register.R11));
        instructions.add(ArmTileHelper.LEAVE);
    }

    @Override
    public TileSet<ArmInstruction, Size> getTiles() {
        return TileSet.getOrMake(Arm32Platform.class);
    }

    @Override
    public TileHelper<ArmInstruction, Size> getTileHelper() {
        return Arm32TileHelper.instance;
    }

    @Override
    public void zeroDefaultLocation(Addable<ArmInstruction> instructions) {
        instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
    }

    @Override
    public void moveStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        instructions.addAll(ArmSizeHelper.storeStatic(Register.R0, Register.R1, staticLbl, sizeHelper));
    }

    @Override
    public void zeroStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
        instructions.addAll(ArmSizeHelper.storeStatic(Register.R0, Register.R1, staticLbl, sizeHelper));
    }

    @Override
    public void moveStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        instructions.addAll(ArmSizeHelper.storeStaticLong(Register.R0, Register.R2, Register.R4, staticLbl,
                sizeHelper));
    }

    @Override
    public void zeroStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
        instructions.addAll(ArmSizeHelper.storeStaticLong(Register.R0, Register.R0, Register.R4, staticLbl,
                sizeHelper));
    }

    public static class Factory implements ArmPlatformFactory<Arm32Platform> {
        public static final Factory FACTORY = new Factory();

        private Factory() {}

        @Override
        public Arm32Platform getPlatform(Set<String> opts) {
            return new Arm32Platform(opts);
        }
    }
}
