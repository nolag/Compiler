package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;

@SuppressWarnings("rawtypes")
public class InstanceOfTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, InstanceOfExprSymbol> {

    private static InstanceOfTile tile;

    private InstanceOfTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> InstanceOfTile<T, E> getTile() {
        if (tile == null) {
            tile = new InstanceOfTile();
        }
        return tile;
    }

    @Override
    public boolean fits(InstanceOfExprSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(InstanceOfExprSymbol op, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TileHelper<T, E> tileHelper = platform.getTileHelper();

        instructions.addAll(platform.getBest(op.getLeftOperand()));
        // eax should have reference to object
        String nullObjectLbl = "nullObject" + CodeGenVisitor.getNewLblNum();
        tileHelper.setupJmpNull(nullObjectLbl, sizeHelper, instructions);

        instructions.addAll(platform.getObjectLayout().subtypeCheckCode((TypeSymbol) op.getRightOperand(), platform));

        String endLbl = "instanceOfEnd" + CodeGenVisitor.getNewLblNum();
        tileHelper.setupJump(endLbl, sizeHelper, instructions);

        tileHelper.setupLbl(nullObjectLbl, instructions);
        tileHelper.setupComment("set to false", instructions);
        tileHelper.loadBool(false, instructions, sizeHelper);

        tileHelper.setupLbl(endLbl, instructions);
        return instructions;
    }
}
