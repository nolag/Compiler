package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Movt;
import cs444.codegen.arm.instructions.Movw;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class LongStaticNameValue extends LongOnlyTile<ArmInstruction, Size, SimpleNameSymbol> {
    private static LongStaticNameValue tile;

    public static LongStaticNameValue getTile() {
        if (tile == null) tile = new LongStaticNameValue();
        return tile;
    }

    @Override
    public boolean fits(final SimpleNameSymbol name, final Platform<ArmInstruction, Size> platform) {
        return super.fits(name, platform) && name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(SimpleNameSymbol name, Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = PkgClassResolver.getUniqueNameFor(dcl);

        instructions.add(new Comment("Moving " + staticFieldLbl + " into R0, R2"));
        instructions.add(new Movw(Register.R0, new ImmediateStr(":lower16:" + staticFieldLbl), sizeHelper));
        instructions.add(new Movt(Register.R0, new ImmediateStr(":upper16:" + staticFieldLbl), sizeHelper));
        instructions.add(new Ldr(Register.R2, Register.R0, Immediate8.FOUR, sizeHelper));
        instructions.add(new Ldr(Register.R0, Register.R0, sizeHelper));

        return instructions;
    }
}
