package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class SizedBinOpTile<T extends BinOpExpr> extends BinOpTile<T>{

    protected SizedBinOpTile(final BinOpMaker maker) {
        super(maker);
    }

    @Override
    public boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        return !op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);
    }
}
