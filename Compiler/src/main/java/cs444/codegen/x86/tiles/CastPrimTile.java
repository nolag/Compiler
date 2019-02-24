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

    private CastPrimTile() {}

    public static CastPrimTile getTile() {
        if (tile == null) {
            tile = new CastPrimTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CastExpressionSymbol symbol, Platform<X86Instruction, Size> platform) {
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        Typeable typeable = (Typeable) symbol.getOperandExpression();
        boolean b =
                sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(typeable.getType().getTypeDclNode().fullName);
        return super.fits(symbol, platform) && !TileHelper.isReferenceType(symbol) && b;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(CastExpressionSymbol symbol,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        TypeSymbol type = symbol.getType();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        Size curSize = sizeHelper.getSize(type.getTypeDclNode().getRealSize(sizeHelper));

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        X86TileHelper.genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type, sizeHelper, instructions);
        return instructions;
    }
}
