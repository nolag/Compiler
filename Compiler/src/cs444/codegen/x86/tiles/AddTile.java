package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AddOpMaker;
import cs444.codegen.x86.tiles.helpers.SizedBinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;

public class AddTile extends SizedBinOpTile<AddExprSymbol> {
    private static AddTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass){
        if(tile == null) tile = new AddTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).adds.add(tile);
    }

    private AddTile(){
        super(AddOpMaker.maker, false);
    }

    @Override
    public boolean fits(final AddExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform) && !op.getType().value.equals(JoosNonTerminal.STRING);
    }
}