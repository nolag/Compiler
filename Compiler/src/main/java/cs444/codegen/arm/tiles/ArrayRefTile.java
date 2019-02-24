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

    private ArrayRefTile() {}

    public static ArrayRefTile getTile() {
        if (tile == null) {
            tile = new ArrayRefTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(ArrayAccessExprSymbol arrayAccess,
                                                          Platform<ArmInstruction, Size> platform) {

        InstructionsAndTiming<ArmInstruction> instructions = super.generate(arrayAccess, platform);
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Add(Register.R0, Register.R0, Register.R8, sizeHelper));
        instructions.add(new Pop(Register.R8));
        return instructions;
    }

    @Override
    public boolean fits(ArrayAccessExprSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return true;
    }
}
