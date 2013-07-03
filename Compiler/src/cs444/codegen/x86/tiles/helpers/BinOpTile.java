package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.BinOpMaker;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinOpTile<T extends BinOpExpr> implements ITile<X86Instruction, T>{
    private final BinOpMaker maker;

    protected BinOpTile(final BinOpMaker maker){
        this.maker = maker;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();

        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.addAll(platform.getBest(bin.children.get(0)));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(bin.children.get(1)));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(maker.make(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
