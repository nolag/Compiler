package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate16;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Movt;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.INumericLiteral;

public class NumericalTile extends NumericHelperTile<ArmInstruction, Size, INumericLiteral> {
    private static NumericalTile tile;

    public static void init(final Class<? extends Platform<ArmInstruction, Size>> klass) {
        if (tile == null) tile = new NumericalTile();
        TileSet.<ArmInstruction, Size> getOrMake(klass).numbs.add(tile);
    }

    private NumericalTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final INumericLiteral num, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        long n = num.getAsLongValue();
        if (n >= 0) {
            if (n <= 65535) {
                instructions.add(new Mov(Register.R0, new Immediate16((char) n), sizeHelper));
                return instructions;
            }
            //TODO other number optimizations that are shifts
        }

        int ival = (int) n;
        instructions.add(new Mov(Register.R0, new Immediate16(ival & 0xFFFF), sizeHelper));
        instructions.add(new Movt(Register.R0, new Immediate16(ival & 0xFFFF0000 >> 16), sizeHelper));
        return instructions;
    }
}
