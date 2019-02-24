package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.OrExprSymbol;

@SuppressWarnings("rawtypes")
public class OrTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, OrExprSymbol> {

    private static OrTile tile;

    private OrTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> OrTile<T, E> getTile() {
        if (tile == null) {
            tile = new OrTile();
        }
        return tile;
    }

    @Override
    public boolean fits(OrExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(OrExprSymbol op, Platform<T, E> platform) {
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        TileHelper<T, E> tileHelper = platform.getTileHelper();

        String orEnd = "or" + CodeGenVisitor.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        tileHelper.setupJumpNeFalse(orEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        tileHelper.setupJumpNeFalse(orEnd, sizeHelper, instructions);
        tileHelper.setupLbl(orEnd, instructions);

        return instructions;
    }
}
