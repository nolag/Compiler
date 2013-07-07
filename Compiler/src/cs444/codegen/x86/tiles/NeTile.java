package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.SetneMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.NeExprSymbol;

public class NeTile extends CompOpTile<NeExprSymbol>{
    public static void init(){
        new NeTile();
    }

    private NeTile() {
        super(SetneMaker.maker);
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).nes.add(this);
    }

    @Override
    public boolean fits(final NeExprSymbol symbol) {
        return true;
    }
}
