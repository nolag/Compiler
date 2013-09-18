package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.Thisable;
import cs444.parser.symbols.ast.expressions.CreationExpression;

public class NonNullFieldAccess<T extends Instruction, E extends Enum<E>> implements ITile<T, E, FieldAccessSymbol>{

    public static <T extends Instruction, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass){
        new NonNullFieldAccess<T, E>(klass);
    }

    private NonNullFieldAccess(final Class<? extends Platform<T, E>> klass){
        TileSet.<T, E>getOrMake(klass).fieldAccess.add(this);
    }

    @Override
    public boolean fits(final FieldAccessSymbol symbol, final Platform<T, E> platform) {
        final ISymbol first = symbol.children.get(0);
        return first instanceof Thisable || first instanceof CreationExpression;
    }

    @Override
    public InstructionsAndTiming<T> generate(final FieldAccessSymbol field, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(field.children.get(0)));
        instructions.addAll(platform.getBest(field.children.get(1)));
        return instructions;
    }
}
