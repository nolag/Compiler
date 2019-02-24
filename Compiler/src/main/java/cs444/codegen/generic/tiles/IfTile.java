package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;

@SuppressWarnings("rawtypes")
public class IfTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, IfExprSymbol> {

    private static IfTile tile;

    private IfTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> IfTile<T, E> getTile() {
        if (tile == null) {
            tile = new IfTile();
        }
        return tile;
    }

    @Override
    public boolean fits(IfExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(IfExprSymbol ifExprSymbol, Platform<T, E> platform) {
        TileHelper<T, E> tileHelper = platform.getTileHelper();

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();

        long myid = CodeGenVisitor.getNewLblNum();
        tileHelper.setupComment("if start" + myid, instructions);
        String falseLbl = "false" + myid;
        String trueLbl = "true" + myid;
        instructions.addAll(platform.getBest(ifExprSymbol.getConditionSymbol()));

        tileHelper.setupJumpNe(falseLbl, sizeHelper, instructions);

        instructions.addAll(platform.getBest(ifExprSymbol.getifBody()));

        tileHelper.setupJump(trueLbl, sizeHelper, instructions);
        tileHelper.setupLbl(falseLbl, instructions);

        ISymbol elseSymbol = ifExprSymbol.getElseBody();

        if (elseSymbol != null) {
            instructions.addAll(platform.getBest(elseSymbol));
        }

        tileHelper.setupLbl(trueLbl, instructions);
        tileHelper.setupComment("if end" + myid, instructions);

        return instructions;
    }
}
