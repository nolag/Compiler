package cs444.codegen.tiles.generic;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.tiles.generic.helpers.TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivZeroTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, DivideExprSymbol>{

    public static <T extends Instruction, E extends Enum<E>> void init(final Class<T> klass){
        new DivZeroTile<T, E>(klass);
    }

    private DivZeroTile(final Class<T> klass){
        TileSet.<T, E>getOrMake(klass).divs.add(this);
    }

    @Override
    public boolean fits(final DivideExprSymbol symbol, final Platform<T, E> platform) {
        return TileHelper.isZero(symbol.children.get(1));
    }

    @Override
    public InstructionsAndTiming<T> generate(final DivideExprSymbol op, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(op.children.get(0)));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        return instructions;
    }
}
