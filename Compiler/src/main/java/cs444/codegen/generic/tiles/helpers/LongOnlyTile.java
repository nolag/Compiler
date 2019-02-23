package cs444.codegen.generic.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;

public abstract class LongOnlyTile<T extends Instruction<T>, E extends Enum<E>, S extends Typeable> implements ITile<T, E, S> {
    @Override
    public boolean fits(final S typeable, final Platform<T, E> platform) {
        return typeable.getType().value.equals(JoosNonTerminal.LONG);
    }
}
