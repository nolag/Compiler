package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.B;
import cs444.codegen.arm.instructions.Cmp;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Sdiv;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivTile extends NumericHelperTile<ArmInstruction, Size, DivideExprSymbol> {
    private static DivTile tile = new DivTile();

    public static DivTile getTile() {
        if (tile == null) {
            tile = new DivTile();
        }
        return tile;
    }

    private DivTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(DivideExprSymbol div, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final Typeable t1 = (Typeable) div.children.get(0);
        final Typeable t2 = (Typeable) div.children.get(1);

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

        instructions.add(new Cmp(Register.R0, Immediate8.ZERO, sizeHelper));
        final String safeDiv = "safeDiv" + CodeGenVisitor.getNewLblNum();
        instructions.add(new B(Condition.NE, safeDiv));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(platform.makeLabel(safeDiv));

        instructions.add(new Pop(Register.R1));

        instructions.add(new Sdiv(Register.R0, Register.R1, Register.R0, platform.getSizeHelper()));
        return instructions;
    }
}