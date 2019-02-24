package cs444.codegen.arm.arm32.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class Arm32TileHelper extends ArmTileHelper {
    public static Arm32TileHelper instance = new Arm32TileHelper();

    private Arm32TileHelper() {}

    public static void negLog(
            Register low,
            Register high,
            Addable<ArmInstruction> instructions,
            SizeHelper<ArmInstruction, Size> sizeHelper) {
        instructions.add(new Rsb(true, low, low, Immediate8.ZERO, sizeHelper));
        instructions.add(new Rsc(high, high, Immediate8.ZERO, sizeHelper));
    }

    @Override
    public void makeLong(
            Typeable item, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        if (item.getType().value.equals(JoosNonTerminal.LONG)) {
            return;
        }
        instructions.add(new Comment("cast to long"));
        instructions.add(new Mov(Register.R2, new ConstantShift(Register.R0, (byte) 31, Shift.ASR), sizeHelper));
    }

    @Override
    public void pushLong(
            Typeable item, Addable<ArmInstruction> instructions, SizeHelper<ArmInstruction, Size> sizeHelper) {
        makeLong(item, instructions, sizeHelper);
        instructions.add(new Push(Register.R2));
        instructions.add(new Push(Register.R0));
    }

    @Override
    public void loadNumberToDefault(
            INumericLiteral numeric, Addable<ArmInstruction> instructions,
            SizeHelper<ArmInstruction, Size> sizeHelper) {
        long value = numeric.getAsLongValue();
        setupNumberLoad(Register.R0, (int) value, instructions, sizeHelper);
        if (numeric instanceof LongLiteralSymbol) {
            if (value > 0 && value < Integer.MAX_VALUE) {
                instructions.add(new Eor(Register.R2, Register.R2, Register.R2, sizeHelper));
            } else {
                setupNumberLoad(Register.R2, (int) (value >> 32), instructions, sizeHelper);
            }
        }
    }
}
