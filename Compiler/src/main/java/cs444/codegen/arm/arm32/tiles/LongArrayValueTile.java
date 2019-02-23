package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArrayBaseTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class LongArrayValueTile extends ArrayBaseTile {
    private static LongArrayValueTile tile;

    public static LongArrayValueTile getTile() {
        if (tile == null) tile = new LongArrayValueTile();
        return tile;
    }

    private LongArrayValueTile() {}

    @Override
    public final boolean fits(final ArrayAccessExprSymbol typeable, final Platform<ArmInstruction, Size> platform) {
        return typeable.getType().value.equals(JoosNonTerminal.LONG);
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = super.generate(arrayAccess, platform);
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Add(Register.R0, Register.R0, Register.R8, sizeHelper));
        instructions.add(new Ldr(Register.R2, Register.R0, Immediate8.FOUR, sizeHelper));
        instructions.add(new Ldr(Register.R0, Register.R0, sizeHelper));

        instructions.add(new Pop(Register.R8));
        return instructions;
    }
}
