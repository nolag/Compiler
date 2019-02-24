package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Rsb;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class NegTile extends NumericHelperTile<ArmInstruction, Size, NegOpExprSymbol> {
    private static NegTile tile;

    private NegTile() {}

    public static NegTile getTile() {
        if (tile == null) {
            tile = new NegTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(NegOpExprSymbol neg,
                                                          Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        instructions.addAll(platform.getBest(neg.children.get(0)));
        instructions.add(new Rsb(Register.R0, Register.R0, Immediate8.ZERO, platform.getSizeHelper()));
        return instructions;
    }
}