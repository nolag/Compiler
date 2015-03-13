package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Rsb;
import cs444.codegen.arm.instructions.Rsc;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.NegOpExprSymbol;

public class LongNegTile extends LongOnlyTile<ArmInstruction, Size, NegOpExprSymbol> {
    private static LongNegTile tile;

    public static LongNegTile getTile() {
        if (tile == null) tile = new LongNegTile();
        return tile;
    }

    private LongNegTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(NegOpExprSymbol neg, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(neg.children.get(0)));
        instructions.add(new Rsb(Register.R0, Register.R0, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsc(Register.R2, Register.R2, Immediate8.ZERO, sizeHelper));
        return instructions;
    }
}