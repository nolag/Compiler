package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.Typeable;

public class LongDclTile extends LongOnlyTile<ArmInstruction, Size, DclSymbol> {
    private static LongDclTile tile;

    public static LongDclTile getTile() {
        if (tile == null) tile = new LongDclTile();
        return tile;
    }

    private LongDclTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final DclSymbol dclSymbol, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        if (dclSymbol.children.isEmpty()) {
            instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
            final ArmInstruction push0 = new Push(Register.R0);
            instructions.add(push0);
            instructions.add(push0);
        } else {
            final Typeable init = (Typeable) dclSymbol.children.get(0);
            instructions.addAll(platform.getBest(init));
            tileHelper.pushLong(init, instructions, sizeHelper);
        }

        return instructions;
    }
}
