package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.OrOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;

public class EOrTile extends BinOpTile<EOrExprSymbol> {
    public static void init(){
        new EOrTile();
    }

    private EOrTile(){
        super(OrOpMaker.maker);
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).eors.add(this);
    }

    @Override
    public boolean fits(final EOrExprSymbol op) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
