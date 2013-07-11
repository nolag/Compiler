package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.RSOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class RSTile extends BinOpTile<RSExprSymbol> {
    public static void init(){
        new RSTile();
    }

    private RSTile(){
        super(RSOpMaker.maker, true);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).rss.add(this);
    }

    @Override
    public boolean fits(final RSExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
