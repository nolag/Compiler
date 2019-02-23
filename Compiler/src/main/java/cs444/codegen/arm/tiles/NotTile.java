package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.NotOpExprSymbol;

public class NotTile implements ITile<ArmInstruction, Size, NotOpExprSymbol> {
    private static NotTile tile;

    public static NotTile getTile() {
        if (tile == null) tile = new NotTile();
        return tile;
    }

    private NotTile() {}

    @Override
    public boolean fits(final NotOpExprSymbol op, final Platform<ArmInstruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final NotOpExprSymbol notSymbol, final Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(notSymbol.children.get(0)));
        instructions.add(new Eor(Register.R0, Register.R0, Immediate8.TRUE, sizeHelper));
        return instructions;
    }

}
