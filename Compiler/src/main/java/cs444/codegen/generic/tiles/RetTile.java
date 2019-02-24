package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;

@SuppressWarnings("rawtypes")
public class RetTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, ReturnExprSymbol> {

    private static RetTile tile;

    private RetTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> RetTile<T, E> getTile() {
        if (tile == null) {
            tile = new RetTile();
        }
        return tile;
    }

    @Override
    public boolean fits(ReturnExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(ReturnExprSymbol retSymbol, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        if (retSymbol.children.size() == 1) {
            instructions.addAll(platform.getBest(retSymbol.children.get(0)));
        }
        platform.getTileHelper().methEpilogue(platform.getSizeHelper(), instructions);
        return instructions;
    }
}
