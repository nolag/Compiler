package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.RegisterShift;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ShiftTile<T extends BinOpExpr> extends NumericHelperTile<ArmInstruction, Size, T> {
    private static final Map<Shift, ShiftTile> tiles = new EnumMap<>(Shift.class);
    private final Shift type;

    private ShiftTile(Shift type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> ShiftTile<T> getTile(Shift type) {
        ShiftTile tile = tiles.get(type);
        if (tile == null) {
            tile = new ShiftTile(type);
            tiles.put(type, tile);
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        Typeable t1 = (Typeable) bin.children.get(0);
        Typeable t2 = (Typeable) bin.children.get(1);

        TypeSymbol ts1 = t1.getType();

        boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.addAll(platform.getBest(t1));
        if (hasLong) {
            platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        }
        instructions.add(new Push(Register.R0));

        instructions.addAll(platform.getBest(t2));
        instructions.add(new Pop(Register.R1));

        instructions.add(new Mov(Register.R0, new RegisterShift(Register.R1, Register.R0, type), sizeHelper));

        return instructions;
    }
}
