package cs444.codegen.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;

public interface ITile<T extends Instruction<T>, E extends Enum<E>, S extends ISymbol> {
    public boolean fits(final S symbol, final Platform<T, E> platform);
    public InstructionsAndTiming<T> generate(final S symbol, final Platform<T, E> platform);
}
