package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.URSOpMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.URSExprSymbol;

public class URSTile extends BinOpTile<URSExprSymbol> {
    public static void init(){
        new URSTile();
    }

    private URSTile(){
        super(URSOpMaker.maker, true);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).urss.add(this);
    }

    @Override
    public boolean fits(final URSExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.INTEGER);
    }
}
