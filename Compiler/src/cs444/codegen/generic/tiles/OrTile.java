package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;

public class OrTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, OrExprSymbol> {
    private static OrTile<?, ?> tile;

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass) {
        if (tile == null) tile = new OrTile<T, E>();
        TileSet.<T, E> getOrMake(klass).ors.add((ITile<T, E, OrExprSymbol>) tile);
    }

    private OrTile() {}

    @Override
    public boolean fits(final OrExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final OrExprSymbol op, final Platform<T, E> platform) {
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        final String orEnd = "or" + CodeGenVisitor.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        tileHelper.setupJumpNeFalse(orEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        tileHelper.setupJumpNeFalse(orEnd, sizeHelper, instructions);
        tileHelper.setupLbl(orEnd, instructions);

        return instructions;
    }
}
