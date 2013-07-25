package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class LongCastTile extends LongOnlyTile<CastExpressionSymbol> {
    public static void init(){
        new LongCastTile();
    }

    private LongCastTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).casts.add(this);
    }

    @Override
    public boolean fits(final CastExpressionSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return super.fits(symbol, platform) && !X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CastExpressionSymbol symbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final Typeable from = (Typeable)symbol.getOperandExpression();


        instructions.addAll(platform.getBest(from));
        platform.getTileHelper().makeLong(from, instructions, sizeHelper);

        return instructions;
    }
}
