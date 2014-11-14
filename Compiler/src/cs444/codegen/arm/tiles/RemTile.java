package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.B;
import cs444.codegen.arm.instructions.Cmp;
import cs444.codegen.arm.instructions.Muls;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Sdiv;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class RemTile extends NumericHelperTile<ArmInstruction, Size, RemainderExprSymbol> {
    private static RemTile tile = new RemTile();

    public static RemTile getTile() {
        if (tile == null) {
            tile = new RemTile();
        }
        return tile;
    }

    private RemTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(RemainderExprSymbol rem, Platform<ArmInstruction, Size> platform) {
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final Typeable t1 = (Typeable) rem.children.get(0);
        final Typeable t2 = (Typeable) rem.children.get(1);

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
        instructions.add(new B(new ImmediateStr(safeDiv)));
        platform.getRunime().throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(platform.makeLabel(safeDiv));

        instructions.add(new Pop(Register.R1));

        instructions.add(new Sdiv(Register.R2, Register.R1, Register.R0, platform.getSizeHelper()));

        instructions.add(new Muls(Register.R0, Register.R1, Register.R2, Register.R0, sizeHelper));

        return instructions;
    }
}