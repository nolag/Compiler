package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;

@SuppressWarnings("rawtypes")
public class WhileTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, WhileExprSymbol> {
    private static WhileTile tile;

    private WhileTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> WhileTile<T, E> getTile() {
        if (tile == null) {
            tile = new WhileTile();
        }
        return tile;
    }

    @Override
    public boolean fits(WhileExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(WhileExprSymbol whileExprSymbol, Platform<T, E> platform) {

        TileHelper<T, E> tileHelper = platform.getTileHelper();

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        long mynum = CodeGenVisitor.getNewLblNum();
        tileHelper.setupComment("while start " + mynum, instructions);
        String loopStart = "loopStart" + mynum;
        String loopEnd = "loopEnd" + mynum;

        tileHelper.setupLbl(loopStart, instructions);
        instructions.addAll(platform.getBest(whileExprSymbol.getConditionSymbol()));

        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        platform.getTileHelper().setupJumpNe(loopEnd, sizeHelper, instructions);

        instructions.addAll(platform.getBest((whileExprSymbol.getBody())));

        tileHelper.setupJump(loopStart, sizeHelper, instructions);
        tileHelper.setupLbl(loopEnd, instructions);
        tileHelper.setupComment("while end " + mynum, instructions);

        return instructions;
    }
}
