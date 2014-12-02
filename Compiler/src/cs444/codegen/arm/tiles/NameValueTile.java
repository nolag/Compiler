package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;

public class NameValueTile extends NumericHelperTile<ArmInstruction, Size, SimpleNameSymbol> {
    private static NameValueTile tile;

    public static NameValueTile getTile() {
        if (tile == null) tile = new NameValueTile();
        return tile;
    }

    private NameValueTile() {}

    @Override
    public boolean fits(final SimpleNameSymbol name, final Platform<ArmInstruction, Size> platform) {
        return super.fits(name, platform) && !name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(SimpleNameSymbol name, Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final DclSymbol dcl = name.dcl;
        final Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));

        Register base = dcl.isLocal ? Register.INTRA_PROCEDURE : Register.R0;
        final Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, (int) dcl.getOffset(platform), instructions, sizeHelper);
        instructions.add(new Add(Register.R0, base, op2, sizeHelper));
        instructions.add(new Ldr(size, Register.R0, Register.R0, sizeHelper));

        return instructions;
    }
}
