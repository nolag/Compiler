package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public abstract class NumericArrayTile extends ArrayBaseTile {
    @Override
    public final boolean fits(final ArrayAccessExprSymbol access, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(access.getType().getTypeDclNode().fullName);
    }
}
