package cs444.codegen.arm.arm32.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class Arm32TileHelper extends ArmTileHelper {
    public static Arm32TileHelper instance = new Arm32TileHelper();

    private Arm32TileHelper() {}

    @Override
    public void makeLong(final Typeable item, final Addable<ArmInstruction> instructions, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        //TODO make long
    }

    @Override
    public void pushLong(final Typeable item, final Addable<ArmInstruction> instructions, final SizeHelper<ArmInstruction, Size> sizeHelper) {
        //TODO push long
    }

    @Override
    public void loadNumberToDefault(final INumericLiteral numeric, Addable<ArmInstruction> instructions,
            SizeHelper<ArmInstruction, Size> sizeHelper) {
        ArmTileHelper.setupNumberLoad(Register.R0, (int) numeric.getAsLongValue(), instructions, sizeHelper);
        if (numeric instanceof LongLiteralSymbol) {
            //TODO long
        }
    }
}
