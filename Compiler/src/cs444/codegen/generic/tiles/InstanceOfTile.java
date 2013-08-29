package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;

public class InstanceOfTile <T extends Instruction, E extends Enum<E>> implements ITile<T, E, InstanceOfExprSymbol>{
    public static <T extends Instruction, E extends Enum<E>> void init(final Class<T> klass){
        new InstanceOfTile<T, E>(klass);
    }

    private InstanceOfTile(final Class<T> klass){
        TileSet.<T, E>getOrMake(klass).insts.add(this);
    }

    @Override
    public boolean fits(final InstanceOfExprSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final InstanceOfExprSymbol op, final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        instructions.addAll(platform.getBest(op.getLeftOperand()));
        // eax should have reference to object
        final String nullObjectLbl = "nullObject" + CodeGenVisitor.getNewLblNum();
        tileHelper.ifNullJmpCode(nullObjectLbl, sizeHelper, instructions);

        instructions.addAll(platform.getObjectLayout().subtypeCheckCode((TypeSymbol) op.getRightOperand(), platform));

        final String endLbl = "instanceOfEnd" + CodeGenVisitor.getNewLblNum();
        tileHelper.setupJump(endLbl, sizeHelper, instructions);

        tileHelper.setupLbl(nullObjectLbl, instructions);
        tileHelper.setupComment("set to false", instructions);
        tileHelper.loadBool(false, instructions, sizeHelper);

        tileHelper.setupLbl(endLbl, instructions);
        return instructions;
    }
}