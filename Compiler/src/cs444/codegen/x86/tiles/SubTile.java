package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.SubOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.SubtractExprSymbol;

public class SubTile extends BinOpTile<SubtractExprSymbol> {
    public static void init(){
        new SubTile();
    }

    private SubTile(){
        super(SubOpMaker.maker);
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).subs.add(this);
    }

    @Override
    public boolean fits(final SubtractExprSymbol op) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
