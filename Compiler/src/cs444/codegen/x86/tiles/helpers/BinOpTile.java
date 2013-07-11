package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.BinOpMaker;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinOpTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T>{
    private final BinOpMaker maker;
    private final boolean forceCl;

    protected BinOpTile(final BinOpMaker maker){
        this(maker, false);
    }

    protected BinOpTile(final BinOpMaker maker, final boolean forceCl){
        this.maker = maker;
        this.forceCl = forceCl;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.addAll(platform.getBest(bin.children.get(0)));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(bin.children.get(1)));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        Register reg = Register.ACCUMULATOR;
        if(forceCl){
            instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));
            reg = Register.COUNTER;
        }
        instructions.add(maker.make(Register.BASE, reg, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
