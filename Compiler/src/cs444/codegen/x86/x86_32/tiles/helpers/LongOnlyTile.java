package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.tiles.ITile;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;

public abstract class LongOnlyTile<T extends Typeable> implements ITile<X86Instruction, Size, T>{
    @Override
    public boolean fits(final T typeable, final Platform<X86Instruction, Size> platform) {
        return typeable.getType().value.equals(JoosNonTerminal.LONG);
    }
}
