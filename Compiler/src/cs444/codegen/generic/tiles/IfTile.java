package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;

public class IfTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, IfExprSymbol>{
    public static <T extends Instruction, E extends Enum<E>> void init(final Class<T> klass){
        new IfTile<T, E>(klass);
    }

    private IfTile(final Class<T> klass){
        TileSet.<T, E>getOrMake(klass).ifs.add(this);
    }

    @Override
    public boolean fits(final IfExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final IfExprSymbol ifExprSymbol, final Platform<T, E> platform) {
        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();

        final long myid = CodeGenVisitor.getNewLblNum();
        tileHelper.setupComment("if start" + myid, instructions);
        final String falseLbl = "false" + myid;
        final String trueLbl = "true" + myid;
        instructions.addAll(platform.getBest(ifExprSymbol.getConditionSymbol()));

        tileHelper.setupJumpNe(falseLbl, sizeHelper, instructions);

        instructions.addAll(platform.getBest(ifExprSymbol.getifBody()));

        tileHelper.setupJump(trueLbl, sizeHelper, instructions);
        tileHelper.setupLbl(falseLbl, instructions);

        final ISymbol elseSymbol = ifExprSymbol.getElseBody();

        if(elseSymbol != null) instructions.addAll(platform.getBest(elseSymbol));

        tileHelper.setupLbl(trueLbl, instructions);
        tileHelper.setupComment("if end" + myid, instructions);

        return instructions;
    }
}
