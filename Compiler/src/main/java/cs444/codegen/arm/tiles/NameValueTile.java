package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;

public class NameValueTile extends NumericHelperTile<ArmInstruction, Size, SimpleNameSymbol> {
    private static NameValueTile tile;

    private NameValueTile() {}

    public static NameValueTile getTile() {
        if (tile == null) {
            tile = new NameValueTile();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleNameSymbol name, Platform<ArmInstruction, Size> platform) {
        return super.fits(name, platform) && !name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(SimpleNameSymbol name,
                                                          Platform<ArmInstruction, Size> platform) {
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        DclSymbol dcl = name.dcl;
        Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));

        Register base = dcl.isLocal ? Register.INTRA_PROCEDURE : Register.R0;
        Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, (int) dcl.getOffset(platform), instructions,
                sizeHelper);
        instructions.add(new Add(Register.R0, base, op2, sizeHelper));
        if (!JoosNonTerminal.unsigned.contains(name.getType().getTypeDclNode().fullName)) {
            size = ArmSizeHelper.getSignedSize(size);
        }
        instructions.add(new Ldr(size, Register.R0, Register.R0, sizeHelper));

        return instructions;
    }
}
