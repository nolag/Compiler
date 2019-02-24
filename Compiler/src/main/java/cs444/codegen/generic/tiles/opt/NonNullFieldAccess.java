package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.Thisable;
import cs444.parser.symbols.ast.expressions.CreationExpression;

@SuppressWarnings("rawtypes")
public class NonNullFieldAccess<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, FieldAccessSymbol> {
    private static NonNullFieldAccess tile;

    private NonNullFieldAccess() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> NonNullFieldAccess<T, E> getTile() {
        if (tile == null) {
            tile = new NonNullFieldAccess();
        }
        return tile;
    }

    @Override
    public boolean fits(FieldAccessSymbol symbol, Platform<T, E> platform) {
        ISymbol first = symbol.children.get(0);
        return first instanceof Thisable || first instanceof CreationExpression;
    }

    @Override
    public InstructionsAndTiming<T> generate(FieldAccessSymbol field, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(field.children.get(0)));
        instructions.addAll(platform.getBest(field.children.get(1)));
        return instructions;
    }
}
