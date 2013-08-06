package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.OrOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EOrExprSymbol;

public class EOrTile extends BinOpTile<EOrExprSymbol> {
    public static void init(){
        new EOrTile();
    }

    private EOrTile(){
        super(OrOpMaker.maker, false);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).eors.add(this);
    }

    @Override
    public boolean fits(final EOrExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
