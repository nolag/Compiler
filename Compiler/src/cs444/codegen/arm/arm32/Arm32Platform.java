package cs444.codegen.arm.arm32;

import java.util.Collections;
import java.util.Set;

import utils.GenericMaker;
import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.ObjectLayout;
import cs444.codegen.OperatingSystem;
import cs444.codegen.Platform;
import cs444.codegen.arm.ArmPlatform;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.arm32.tiles.helpers.Arm32TileHelper;
import cs444.codegen.arm.arm32.tiles.helpers.Arm32TileInit;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public class Arm32Platform extends ArmPlatform {

    private final OperatingSystem<Arm32Platform>[] oses = GenericMaker.<OperatingSystem<Arm32Platform>> makeArray(new Linux(this));

    public static class Factory implements ArmPlatformFactory<Arm32Platform> {
        public static final Factory FACTORY = new Factory();

        private Factory() {}

        @Override
        public Arm32Platform getPlatform(Set<String> opts) {
            return new Arm32Platform(opts);
        }
    }

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
        Arm32TileHelper.instance.invokeConstructor(resolver, Collections.<ISymbol> emptyList(), this, instructions);
    }

    @Override
    public void genHeaderEnd(APkgClassResolver resolver, Addable<ArmInstruction> instructions) {
        instructions.add(new Push(Register.R11));
        instructions.add(new Comment("Store pointer to object in R11"));
        instructions.add(new Mov(Register.R11, Register.R0, sizeHelper));

        for (final DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            final long offset = fieldDcl.getOffset(this);

            if (!fieldDcl.children.isEmpty()) {
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));

                final CodeGenVisitor<ArmInstruction, Size> visitor = new CodeGenVisitor<ArmInstruction, Size>(
                        CodeGenVisitor.<ArmInstruction, Size> getCurrentCodeGen(this).currentFile, this);

                final ISymbol field = fieldDcl.children.get(0);
                field.accept(visitor);
                instructions.addAll(getBest(field));

                if (fieldDcl.getType().value.equals(JoosNonTerminal.LONG)) {
                    //TODO long
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
        return TileSet.<ArmInstruction, Size> getOrMake(Arm32Platform.class);
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
        instructions.addAll(ArmSizeHelper.storeStatic(Register.R0, staticLbl, sizeHelper));
    }

    @Override
    public void zeroStatic(String staticLbl, Size size, Addable<ArmInstruction> instructions) {
        instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
        instructions.addAll(ArmSizeHelper.storeStatic(Register.R0, staticLbl, sizeHelper));
    }

    @Override
    public void moveStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO static long move

    }

    @Override
    public void zeroStaticLong(String staticLbl, Addable<ArmInstruction> instructions) {
        // TODO zero static long

    }
}
