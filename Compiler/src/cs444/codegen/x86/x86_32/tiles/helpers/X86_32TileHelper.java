package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Adc;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.Cdq;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Not;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.INumericLiteral;
import cs444.parser.symbols.ast.LongLiteralSymbol;
import cs444.parser.symbols.ast.Typeable;

public class X86_32TileHelper extends X86TileHelper {
    public static X86_32TileHelper instance = new X86_32TileHelper();

    private X86_32TileHelper() {}

    @Override
    public void makeLong(final Typeable item, final Addable<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper) {

        if (item.getType().value.equals(JoosNonTerminal.LONG)) return;
        instructions.add(new Comment("cast to long"));
        instructions.add(Cdq.cdq);

    }

    @Override
    public void pushLong(final Typeable item, final Addable<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper) {
        makeLong(item, instructions, sizeHelper);
        instructions.add(new Push(Register.DATA, sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
    }

    public static void negLong(final Register high, final Register low, final Addable<X86Instruction> instructions,
            final SizeHelper<X86Instruction, Size> sizeHelper) {

        instructions.add(new Not(high, sizeHelper));
        instructions.add(new Not(low, sizeHelper));
        instructions.add(new Add(low, Immediate.ONE, sizeHelper));
        instructions.add(new Adc(high, Immediate.ZERO, sizeHelper));
    }

    @Override
    public void loadNumberToDefault(final INumericLiteral numeric, Addable<X86Instruction> instructions,
            SizeHelper<X86Instruction, Size> sizeHelper) {
        final long value = numeric.getAsLongValue();
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate((int) value), Size.DWORD, sizeHelper));

        if (numeric instanceof LongLiteralSymbol) {
            if (value > 0 && value < Integer.MAX_VALUE) instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
            else instructions.add(new Mov(Register.DATA, new Immediate(value >> 32), Size.DWORD, sizeHelper));
        }
    }
}
