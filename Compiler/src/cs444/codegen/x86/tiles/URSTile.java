package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.URSOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.URSExprSymbol;

public class URSTile extends BinOpTile<URSExprSymbol> {
    public static void init(){
        new URSTile();
    }

    private URSTile(){
        super(URSOpMaker.maker, true);
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).urss.add(this);
    }

    @Override
    public boolean fits(final URSExprSymbol op) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
