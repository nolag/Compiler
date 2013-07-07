package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.*;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.UniOpMaker;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinUniOpTile<T extends BinOpExpr> implements ITile<X86Instruction, X86SizeHelper, T>{
    private final UniOpMaker maker;
    private final boolean sar;

    protected BinUniOpTile(final UniOpMaker maker, final boolean sar){
        this.maker = maker;
        this.sar = sar;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, X86SizeHelper> platform) {
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.addAll(platform.getBest(bin.children.get(0)));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(bin.children.get(1)));

        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        // pop first operand
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        // first operand -> eax, second operand -> ebx
        if(sar){
            instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Sar(Register.DATA, new Immediate(sizeHelper.defaultStackSize * 8 -1), sizeHelper));
            final String safeDiv = "safeDiv" + CodeGenVisitor.getNewLblNum();
            TileHelper.setupJumpNe(Register.BASE, Immediate.ZERO, safeDiv, sizeHelper, instructions);
            Runtime.instance.throwException(instructions, "Divide by zero");
            instructions.add(new Label(safeDiv));
        }

        instructions.add(maker.make(Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }

}