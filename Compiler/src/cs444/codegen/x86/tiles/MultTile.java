package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.IMulMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class MultTile extends BinUniOpTile<MultiplyExprSymbol>{
    private MultTile() {
        super(IMulMaker.maker, false);
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).mults.add(this);
    }

    public static void init(){
        new MultTile();
    }

    @Override
    public boolean fits(final MultiplyExprSymbol symbol) {
        return true;
    }
}
