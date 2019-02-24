package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;

@SuppressWarnings("rawtypes")
public class AndTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, AndExprSymbol> {
    private static AndTile tile;

    private AndTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> AndTile<T, E> getTile() {
        if (tile == null) {
            tile = new AndTile();
        }
        return tile;
    }

    @Override
    public boolean fits(AndExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(AndExprSymbol op, Platform<T, E> platform) {
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TileHelper<T, E> tileHelper = platform.getTileHelper();
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        String andEnd = "and" + CodeGenVisitor.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        tileHelper.setupJumpNe(andEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        tileHelper.setupJumpNe(andEnd, sizeHelper, instructions);
        tileHelper.setupLbl(andEnd, instructions);

        return instructions;
    }
}
