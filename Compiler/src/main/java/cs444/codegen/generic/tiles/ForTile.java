package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;

@SuppressWarnings("rawtypes")
public class ForTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, ForExprSymbol> {

    private static ForTile tile;

    private ForTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ForTile<T, E> getTile() {
        if (tile == null) {
            tile = new ForTile();
        }
        return tile;
    }

    @Override
    public boolean fits(ForExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(ForExprSymbol forExprSymbol, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        SizeHelper<T, E> EHelper = platform.getSizeHelper();
        long mynum = CodeGenVisitor.getNewLblNum();
        instructions.add(platform.makeComment("for start " + mynum));
        String loopStart = "loopStart" + mynum;
        String loopEnd = "loopEnd" + mynum;
        TileHelper<T, E> helper = platform.getTileHelper();

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
