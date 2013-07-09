package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastPrimTile implements ITile<X86Instruction, X86SizeHelper, CastExpressionSymbol> {
    public static void init(){
        new CastPrimTile();
    }

    private CastPrimTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).casts.add(this);
    }

    @Override
    public boolean fits(final CastExpressionSymbol symbol) {
        return !X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CastExpressionSymbol symbol,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final TypeSymbol type = symbol.getType();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final Size curSize = sizeHelper.getSize(type.getTypeDclNode().getRealSize(sizeHelper));

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        X86TileHelper.genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type, sizeHelper, instructions);
        return instructions;
    }
}
