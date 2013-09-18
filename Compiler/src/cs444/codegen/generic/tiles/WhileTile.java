package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;

public class WhileTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, WhileExprSymbol>{
    public static <T extends Instruction, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass){
        new WhileTile<T, E>(klass);
    }

    private WhileTile(final Class<? extends Platform<T, E>> klass){
        TileSet.<T, E>getOrMake(klass).whiles.add(this);
    }

    @Override
    public boolean fits(final WhileExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final WhileExprSymbol whileExprSymbol, final Platform<T, E> platform) {

        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final long mynum = CodeGenVisitor.getNewLblNum();
        tileHelper.setupComment("while start " + mynum, instructions);
        final String loopStart = "loopStart" + mynum;
        final String loopEnd = "loopEnd" + mynum;

        tileHelper.setupLbl(loopStart, instructions);
        instructions.addAll(platform.getBest(whileExprSymbol.getConditionSymbol()));

        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        platform.getTileHelper().setupJumpNe(loopEnd, sizeHelper, instructions);

        instructions.addAll(platform.getBest((whileExprSymbol.getBody())));

        tileHelper.setupJump(loopStart, sizeHelper, instructions);
        tileHelper.setupLbl(loopEnd, instructions);
        tileHelper.setupComment("while end " + mynum, instructions);

        return instructions;
    }
}
