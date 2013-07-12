package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.LSOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.LSExprSymbol;

public class LSTile extends BinOpTile<LSExprSymbol> {
    public static void init(){
        new LSTile();
    }

    private LSTile(){
        super(LSOpMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).lss.add(this);
    }

    @Override
    public boolean fits(final LSExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
