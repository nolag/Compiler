package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.IDivMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivTile extends BinUniOpTile<DivideExprSymbol>{
    private DivTile() {
        super(IDivMaker.maker, true);
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).divs.add(this);
    }

    public static void init(){
        new DivTile();
    }

    @Override
    public boolean fits(final DivideExprSymbol symbol) {
        return true;
    }
}
