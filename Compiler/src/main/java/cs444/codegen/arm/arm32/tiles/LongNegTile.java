package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.arm32.tiles.helpers.Arm32TileHelper;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class LongNegTile extends LongOnlyTile<ArmInstruction, Size, NegOpExprSymbol> {
    private static LongNegTile tile;

    private LongNegTile() {}

    public static LongNegTile getTile() {
        if (tile == null) {
            tile = new LongNegTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(NegOpExprSymbol neg,
                                                          Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(neg.children.get(0)));
        Arm32TileHelper.negLog(Register.R0, Register.R2, instructions, sizeHelper);
        return instructions;
    }
}