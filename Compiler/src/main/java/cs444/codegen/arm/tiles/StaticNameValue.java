package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Movt;
import cs444.codegen.arm.instructions.Movw;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class StaticNameValue extends NumericHelperTile<ArmInstruction, Size, SimpleNameSymbol> {
    private static StaticNameValue tile;

    public static StaticNameValue getTile() {
        if (tile == null) {
            tile = new StaticNameValue();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleNameSymbol name, Platform<ArmInstruction, Size> platform) {
        return super.fits(name, platform) && name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(SimpleNameSymbol name,
                                                          Platform<ArmInstruction, Size> platform) {
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        DclSymbol dcl = name.dcl;
        String staticFieldLbl = PkgClassResolver.getUniqueNameFor(dcl);

        instructions.add(new Comment("Moving " + staticFieldLbl + " into R0"));
        instructions.add(new Movw(Register.R0, new ImmediateStr(":lower16:" + staticFieldLbl), sizeHelper));
        instructions.add(new Movt(Register.R0, new ImmediateStr(":upper16:" + staticFieldLbl), sizeHelper));
        instructions.add(new Ldr(Register.R0, Register.R0, sizeHelper));
        return instructions;
    }
}
