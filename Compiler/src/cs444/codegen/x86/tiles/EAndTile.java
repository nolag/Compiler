package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.AndOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;

public class EAndTile extends BinOpTile<EAndExprSymbol> {
    public static void init(){
        new EAndTile();
    }

    private EAndTile(){
        super(AndOpMaker.maker);
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).eands.add(this);
    }

    @Override
    public boolean fits(final EAndExprSymbol op) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
