package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.factories.BinOpRegMaker;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinOpTile<T extends BinOpExpr> implements ITile<ArmInstruction, Size, T> {
    private final BinOpRegMaker maker;

    protected BinOpTile(BinOpRegMaker maker) {
        this.maker = maker;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();

        Typeable t1 = (Typeable) bin.children.get(0);
        Typeable t2 = (Typeable) bin.children.get(1);

        TypeSymbol ts1 = t1.getType();
        TypeSymbol ts2 = t2.getType();

        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.addAll(platform.getBest(t1));
        if (hasLong) {
            platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        }
        instructions.add(new Push(Register.R0));

        instructions.addAll(platform.getBest(t2));
        if (hasLong) {
            platform.getTileHelper().makeLong(t2, instructions, sizeHelper);
        }

        instructions.add(new Pop(Register.R1));

        instructions.add(maker.make(true, Register.R0, Register.R1, Register.R0, platform.getSizeHelper()));

        return instructions;
    }
}
