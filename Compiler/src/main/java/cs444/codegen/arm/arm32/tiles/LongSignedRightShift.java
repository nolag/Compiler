package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Sbfx;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class LongSignedRightShift extends LongShiftsTile<RSExprSymbol> {
    private static LongSignedRightShift tile;

    private LongSignedRightShift() {
        super(Shift.LSR, Shift.LSL, Shift.ASR, true);
    }

    public static LongSignedRightShift getTile() {
        if (tile == null) {
            tile = new LongSignedRightShift();
        }
        return tile;
    }

    @Override
    protected ArmInstruction getLargeEnd(InstructionsAndTiming<ArmInstruction> instructions,
                                         Register second,
                                         SizeHelper<ArmInstruction, Size> sizeHelper) {
        return new Sbfx(Condition.MI, second, second, Immediate8.THIRTY_ONE, Immediate8.ONE);
    }
}
