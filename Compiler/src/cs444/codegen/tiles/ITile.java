package cs444.codegen.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;

public interface ITile<T extends Instruction, S extends ISymbol> {
    public boolean fits(final S symbol);
    public InstructionsAndTiming<T> generate(final S symbol, final Platform<T> platform);
}
