package cs444.codegen.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.FieldAccessSymbol;
import cs444.parser.symbols.ast.Thisable;
import cs444.parser.symbols.ast.expressions.CreationExpression;

public class NonNullFieldAccess<T extends Instruction, U extends SizeHelper<T>> implements ITile<T, U, FieldAccessSymbol>{

    public static <T extends Instruction, U extends SizeHelper<T>> void init(final Class<T> klass){
        new NonNullFieldAccess<T, U>(klass);
    }

    private NonNullFieldAccess(final Class<T> klass){
        TileSet.<T, U>getOrMake(klass).fieldAccess.add(this);
    }

    @Override
    public boolean fits(final FieldAccessSymbol symbol) {
        final ISymbol first = symbol.children.get(0);
        return first instanceof Thisable || first instanceof CreationExpression;
    }

    @Override
    public InstructionsAndTiming<T> generate(final FieldAccessSymbol field, final Platform<T, U> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(field.children.get(0)));
        instructions.addAll(platform.getBest(field.children.get(1)));
        return instructions;
    }
}
