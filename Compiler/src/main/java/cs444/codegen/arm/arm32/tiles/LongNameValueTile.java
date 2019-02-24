package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;

public class LongNameValueTile extends LongOnlyTile<ArmInstruction, Size, SimpleNameSymbol> {
    private static LongNameValueTile tile;

    private LongNameValueTile() {}

    public static LongNameValueTile getTile() {
        if (tile == null) {
            tile = new LongNameValueTile();
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
        instructions.add(new Ldr(size, Register.R2, Register.R0, Immediate8.FOUR, sizeHelper));
        instructions.add(new Ldr(size, Register.R0, Register.R0, sizeHelper));

        return instructions;
    }
}
