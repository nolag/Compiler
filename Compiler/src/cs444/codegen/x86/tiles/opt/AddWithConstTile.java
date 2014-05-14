package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AddOpMaker;
import cs444.codegen.x86.tiles.helpers.BinWithConstTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class AddWithConstTile extends BinWithConstTile<AddExprSymbol> {
    private static AddWithConstTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new AddWithConstTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).adds.add(tile);
    }

    private AddWithConstTile() {
        super(AddOpMaker.maker);
    }

    @Override
    public boolean fits(final AddExprSymbol bin, final Platform<X86Instruction, Size> platform) {
        return super.fits(bin, platform) && !bin.getType().value.equals(JoosNonTerminal.STRING);
    }
}