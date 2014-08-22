package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;

public class ForTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, ForExprSymbol> {
    public static <T extends Instruction<T>, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass) {
        new ForTile<T, E>(klass);
    }

    private ForTile(final Class<? extends Platform<T, E>> klass) {
        TileSet.<T, E> getOrMake(klass).fors.add(this);
    }

    @Override
    public boolean fits(final ForExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final ForExprSymbol forExprSymbol, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> EHelper = platform.getSizeHelper();
        final long mynum = CodeGenVisitor.getNewLblNum();
        instructions.add(platform.makeComment("for start " + mynum));
        final String loopStart = "loopStart" + mynum;
        final String loopEnd = "loopEnd" + mynum;
        final TileHelper<T, E> helper = platform.getTileHelper();

        instructions.add(platform.makeComment("Init for " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getForInit()));
        instructions.add(platform.makeLabel(loopStart));
        instructions.add(platform.makeComment("Compare for " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getConditionExpr()));

        helper.setupJumpNe(loopEnd, EHelper, instructions);

        instructions.add(platform.makeComment("for body" + mynum));

        instructions.addAll(platform.getBest(forExprSymbol.getBody()));

        instructions.add(platform.makeComment("for update " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getForUpdate()));

        platform.getTileHelper().setupJump(loopStart, EHelper, instructions);
        instructions.add(platform.makeLabel(loopEnd));

        //This takes care of the init if they dcl something there
        helper.cleanStackSpace("for loop " + mynum, instructions, forExprSymbol.getStackSize(platform), platform);

        instructions.add(platform.makeComment("for end " + mynum));
        return instructions;
    }
}
