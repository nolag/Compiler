package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.LSOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.LSExprSymbol;

public class LSTile extends BinOpTile<LSExprSymbol> {
    public static void init(){
        new LSTile();
    }

    private LSTile(){
        super(LSOpMaker.maker);
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).lss.add(this);
    }

    @Override
    public boolean fits(final LSExprSymbol op) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
