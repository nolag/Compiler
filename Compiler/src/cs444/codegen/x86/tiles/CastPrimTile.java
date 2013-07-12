package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastPrimTile implements ITile<X86Instruction, Size, CastExpressionSymbol> {
    public static void init(){
        new CastPrimTile();
    }

    private CastPrimTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).casts.add(this);
    }

    @Override
    public boolean fits(final CastExpressionSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return !X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CastExpressionSymbol symbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final TypeSymbol type = symbol.getType();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final Size curSize = sizeHelper.getSize(type.getTypeDclNode().getRealSize(sizeHelper));

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        X86TileHelper.genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type, sizeHelper, instructions);
        return instructions;
    }
}
