package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinOpTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T>{
    private final BinOpMaker maker;

    protected BinOpTile(final BinOpMaker maker){
        this.maker = maker;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final Typeable t1 = (Typeable)bin.children.get(0);
        final Typeable t2 = (Typeable)bin.children.get(1);

        final TypeSymbol ts1 = t1.getType();
        final TypeSymbol ts2 = t2.getType();

        final boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG) ||
                ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);


        instructions.addAll(platform.getBest(t1));
        if(hasLong) platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        
        instructions.addAll(platform.getBest(t2));
        if(hasLong) platform.getTileHelper().makeLong(t2, instructions, sizeHelper);
        instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        instructions.add(maker.make(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));

        return instructions;
    }
}
