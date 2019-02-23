package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public abstract class NumericArrayTile extends ArrayBaseTile {
    @Override
    public final boolean fits(final ArrayAccessExprSymbol access, final Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(access.getType().getTypeDclNode().fullName);
    }
}
