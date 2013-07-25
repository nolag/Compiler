package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;

public class AndTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, AndExprSymbol>{
    public static <T extends Instruction, E extends Enum<E>> void init(final Class<T> klass){
        new AndTile<T, E>(klass);
    }

    private AndTile(final Class<T> klass){
        TileSet.<T, E>getOrMake(klass).ands.add(this);
    }

    @Override
    public boolean fits(final AndExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final AndExprSymbol op, final Platform<T, E> platform){
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        final String andEnd = "and" + CodeGenVisitor.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        tileHelper.setupJumpNe(andEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        tileHelper.setupJumpNe(andEnd, sizeHelper, instructions);
        tileHelper.setupLbl(andEnd, instructions);

        return instructions;
    }
}
