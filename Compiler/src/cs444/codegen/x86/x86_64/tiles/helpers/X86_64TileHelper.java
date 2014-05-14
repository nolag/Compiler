package cs444.codegen.x86.x86_64.tiles.helpers;

import cs444.codegen.Addable;
import cs444.codegen.SizeHelper;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Movsx;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;

public class X86_64TileHelper extends X86TileHelper {
    public static X86_64TileHelper instance = new X86_64TileHelper();

    private X86_64TileHelper(){ }

    @Override
    public void makeLong(final Typeable item,
            final Addable<X86Instruction> instructions, final SizeHelper<X86Instruction, Size> sizeHelper){

        if(item.getType().value.equals(JoosNonTerminal.LONG)) return;
        instructions.add(new Comment("cast to long, since it is already loaded, it is either a long or int in size"));
        instructions.add(new Movsx(Register.ACCUMULATOR, Register.ACCUMULATOR, Size.DWORD, sizeHelper));

    }

    @Override
    public void pushLong(final Typeable item, final Addable<X86Instruction> instructions,
            final SizeHelper<X86Instruction, Size> sizeHelper) {
        makeLong(item, instructions, sizeHelper);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
    }
}
