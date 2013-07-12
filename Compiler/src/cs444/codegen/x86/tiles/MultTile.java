package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.IMulMaker;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class MultTile extends BinUniOpTile<MultiplyExprSymbol>{
    private MultTile() {
        super(IMulMaker.maker, false);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).mults.add(this);
    }

    public static void init(){
        new MultTile();
    }

    @Override
    public boolean fits(final MultiplyExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }
}
