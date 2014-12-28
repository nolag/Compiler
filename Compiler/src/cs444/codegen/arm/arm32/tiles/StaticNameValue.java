package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class StaticNameValue extends NumericHelperTile<ArmInstruction, Size, SimpleNameSymbol> {
    private static StaticNameValue tile;

    public static StaticNameValue getTile() {
        if (tile == null) tile = new StaticNameValue();
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
        instructions.addAll(ArmSizeHelper.loadStatic(Register.R0, staticFieldLbl, sizeHelper));
        return instructions;
    }
}
