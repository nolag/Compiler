package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastPrimTile extends NumericHelperTile<X86Instruction, Size, CastExpressionSymbol> {
    private static CastPrimTile tile;

    public static CastPrimTile getTile() {
        if (tile == null) tile = new CastPrimTile();
        return tile;
    }

    private CastPrimTile() {}

    @Override
    public boolean fits(final CastExpressionSymbol symbol, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final Typeable typeable = (Typeable) symbol.getOperandExpression();
        final boolean b = sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(typeable.getType().getTypeDclNode().fullName);
        return super.fits(symbol, platform) && !TileHelper.isReferenceType(symbol) && b;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CastExpressionSymbol symbol, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final TypeSymbol type = symbol.getType();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final Size curSize = sizeHelper.getSize(type.getTypeDclNode().getRealSize(sizeHelper));

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        X86TileHelper.genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type, sizeHelper, instructions);
        return instructions;
    }
}
