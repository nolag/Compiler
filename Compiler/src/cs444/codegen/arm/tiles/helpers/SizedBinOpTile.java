package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpMaker;
import cs444.codegen.arm.tiles.helpers.BinOpTile;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class SizedBinOpTile<T extends BinOpExpr> extends BinOpTile<T>{

    protected SizedBinOpTile(final BinOpMaker maker) {
        super(maker);
    }

    @Override
    public boolean fits(final T op, final Platform<ArmInstruction, Size> platform) {
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }
}
