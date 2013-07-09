package cs444.codegen.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivZeroTile<T extends Instruction, U extends SizeHelper<T, ?>> implements ITile<T, U, DivideExprSymbol>{

    public static <T extends Instruction, U extends SizeHelper<T, ?>> void init(final Class<T> klass){
        new DivZeroTile<T, U>(klass);
    }

    private DivZeroTile(final Class<T> klass){
        TileSet.<T, U>getOrMake(klass).divs.add(this);
    }

    @Override
    public boolean fits(final DivideExprSymbol symbol) {
        return TileHelper.isZero(symbol.children.get(1));
    }

    @Override
    public InstructionsAndTiming<T> generate(final DivideExprSymbol op, final Platform<T, U> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        instructions.addAll(platform.getBest(op.children.get(0)));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        return instructions;
    }
}
