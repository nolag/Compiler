package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.AndOpMaker;
import cs444.codegen.x86.tiles.helpers.BinOpTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.EAndExprSymbol;

public class EAndTile extends BinOpTile<EAndExprSymbol> {
    public static void init(){
        new EAndTile();
    }

    private EAndTile(){
        super(AndOpMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).eands.add(this);
    }

    @Override
    public boolean fits(final EAndExprSymbol op, final Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.BOOLEAN);
    }
}
