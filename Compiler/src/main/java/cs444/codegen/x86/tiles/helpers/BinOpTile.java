package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinOpTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T> {
    private final BinOpMaker maker;
    private final boolean ordered;

    protected BinOpTile(BinOpMaker maker, boolean ordered) {
        this.maker = maker;
        this.ordered = ordered;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(T bin, Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        Typeable t1 = (Typeable) bin.children.get(0);
        Typeable t2 = (Typeable) bin.children.get(1);

        TypeSymbol ts1 = t1.getType();
        TypeSymbol ts2 = t2.getType();

        boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.addAll(platform.getBest(t1));
        Size size;

        if (hasLong) {
            size = Size.QWORD;
            platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        } else {
            size = Size.DWORD;
        }

        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

        instructions.addAll(platform.getBest(t2));
        if (hasLong) {
            platform.getTileHelper().makeLong(t2, instructions, sizeHelper);
        }

        if (ordered) {
            instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
        } else {
            instructions.add(new Pop(Register.COUNTER, sizeHelper));
        }

        instructions.add(maker.make(Register.ACCUMULATOR, Register.COUNTER, size, sizeHelper));

        return instructions;
    }
}
