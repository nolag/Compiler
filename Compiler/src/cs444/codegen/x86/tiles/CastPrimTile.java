package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastPrimTile implements ITile<X86Instruction, CastExpressionSymbol> {
    public static void init(){
        new CastPrimTile();
    }

    private CastPrimTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).casts.add(this);
    }

    @Override
    public boolean fits(final CastExpressionSymbol symbol) {
        return !TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CastExpressionSymbol symbol, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final TypeSymbol type = symbol.getType();
        final X86SizeHelper sizeHelper = (X86SizeHelper)platform.getSizeHelper();
        final Size curSize = X86SizeHelper.getSize(type.getTypeDclNode().getRealSize(sizeHelper));

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        TileHelper.genMov(curSize, Register.ACCUMULATOR, "cast to " + type.value, type, sizeHelper);
        return instructions;
    }
}
