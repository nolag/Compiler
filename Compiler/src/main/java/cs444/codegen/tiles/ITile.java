package cs444.codegen.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;

public interface ITile<T extends Instruction<T>, E extends Enum<E>, S extends ISymbol> {
    boolean fits(S symbol, Platform<T, E> platform);

    InstructionsAndTiming<T> generate(S symbol, Platform<T, E> platform);
}
