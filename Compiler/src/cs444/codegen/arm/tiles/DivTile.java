package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Sdiv;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivTile extends NumericHelperTile<ArmInstruction, Size, DivideExprSymbol> {
    private static DivTile tile;

    public static DivTile getTile() {
        if (tile == null) tile = new DivTile();
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(DivideExprSymbol symbol, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();

        final Typeable t1 = (Typeable) symbol.children.get(0);
        final Typeable t2 = (Typeable) symbol.children.get(1);

        final TypeSymbol ts1 = t1.getType();
        final TypeSymbol ts2 = t2.getType();

        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        final boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.addAll(platform.getBest(t1));
        if (hasLong) platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        instructions.add(new Push(Register.R0));

        instructions.addAll(platform.getBest(t2));
        if (hasLong) platform.getTileHelper().makeLong(t2, instructions, sizeHelper);

        instructions.add(new Sdiv(Register.R0, Register.R1, Register.R0, sizeHelper));

        return instructions;
    }
}
