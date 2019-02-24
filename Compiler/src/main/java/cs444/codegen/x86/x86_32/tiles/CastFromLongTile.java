package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastFromLongTile implements ITile<X86Instruction, Size, CastExpressionSymbol> {
    private static CastFromLongTile tile;

    private CastFromLongTile() {}

    public static CastFromLongTile getTile() {
        if (tile == null) {
            tile = new CastFromLongTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CastExpressionSymbol symbol, Platform<X86Instruction, Size> platform) {
        Typeable typeable = (Typeable) symbol.getOperandExpression();
        return typeable.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(CastExpressionSymbol symbol,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        TypeSymbol type = symbol.getType();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        Size curSize = sizeHelper.getDefaultSize();

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        X86TileHelper.genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type, sizeHelper, instructions);
        return instructions;
    }
}
