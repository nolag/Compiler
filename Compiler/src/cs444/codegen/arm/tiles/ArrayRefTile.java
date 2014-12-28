package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArrayBaseTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public class ArrayRefTile extends ArrayBaseTile {
    private static ArrayRefTile tile;

    public static ArrayRefTile getTile() {
        if (tile == null) tile = new ArrayRefTile();
        return tile;
    }

    private ArrayRefTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = super.generate(arrayAccess, platform);
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Add(Register.R0, Register.R0, Register.R8, sizeHelper));
        instructions.add(new Pop(Register.R8));
        return instructions;
    }

    @Override
    public boolean fits(final ArrayAccessExprSymbol symbol, final Platform<ArmInstruction, Size> platform) {
        return true;
    }
}
