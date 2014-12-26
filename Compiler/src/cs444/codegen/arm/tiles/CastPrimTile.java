package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Sxtb;
import cs444.codegen.arm.instructions.Sxth;
import cs444.codegen.arm.instructions.Uxtb;
import cs444.codegen.arm.instructions.Uxth;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastPrimTile extends NumericHelperTile<ArmInstruction, Size, CastExpressionSymbol> {
    private static CastPrimTile tile;

    public static CastPrimTile getTile() {
        if (tile == null) tile = new CastPrimTile();
        return tile;
    }

    private CastPrimTile() {}

    @Override
    public boolean fits(final CastExpressionSymbol symbol, final Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final Typeable typeable = (Typeable) symbol.getOperandExpression();
        final boolean b = sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(typeable.getType().getTypeDclNode().fullName);
        return super.fits(symbol, platform) && !TileHelper.isReferenceType(symbol) && b;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final CastExpressionSymbol symbol, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final TypeSymbol type = symbol.getType();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        Size curSize = sizeHelper.getSize(type.getTypeDclNode().getRealSize(sizeHelper));

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));

        if (!JoosNonTerminal.unsigned.contains(type.getTypeDclNode().fullName)) {
            curSize = ArmSizeHelper.getSignedSize(curSize);
        }

        switch (curSize) {
        case B:
            instructions.add(new Uxtb(Register.R0, Register.R0, sizeHelper));
            break;
        case SB:
            instructions.add(new Sxtb(Register.R0, Register.R0, sizeHelper));
            break;
        case H:
            instructions.add(new Uxth(Register.R0, Register.R0, sizeHelper));
            break;
        case SH:
            instructions.add(new Sxth(Register.R0, Register.R0, sizeHelper));
            break;
        default:
            break;
        }

        return instructions;
    }
}
