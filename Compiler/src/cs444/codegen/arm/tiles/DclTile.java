package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.Typeable;

public class DclTile extends NumericHelperTile<ArmInstruction, Size, DclSymbol> {
    private static DclTile tile;

    public static DclTile getTile() {
        if (tile == null) tile = new DclTile();
        return tile;
    }

    private DclTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final DclSymbol dclSymbol, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        if (dclSymbol.children.isEmpty()) {
            instructions.add(new Eor(Register.R0, Register.R0, Register.R0, sizeHelper));
        } else {
            final Typeable child = (Typeable) dclSymbol.children.get(0);
            instructions.addAll(platform.getBest(child));
            if (dclSymbol.getType().value.equals(JoosNonTerminal.LONG)) platform.getTileHelper().makeLong(child, instructions, sizeHelper);
        }

        instructions.add(new Push(Register.R0));
        return instructions;
    }
}
