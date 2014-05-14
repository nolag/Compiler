package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JxxMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class LongJxxTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T>{

    private final JxxMaker finishEarlyT;
    private final JxxMaker finishEarlyF;
    private final JxxMaker finishRegular;

    protected LongJxxTile(final JxxMaker finishEarlyT, final JxxMaker finishEarlyF, final JxxMaker finishRegular) {
        this.finishEarlyT = finishEarlyT;
        this.finishEarlyF = finishEarlyF;
        this.finishRegular = finishRegular;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();
        final long endNum = CodeGenVisitor.getNewLblNum();
        final String endStr = "cmpEnd" + endNum;
        final String endFalseStr = "cmpFalse" + endNum;
        final Immediate end = new Immediate(endStr);
        final Immediate endFalse = new Immediate(endFalseStr);

        instructions.add(new Comment("Start comparison "));

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Immediate.TRUE, sizeHelper));

        final Typeable lhs = (Typeable)bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Push(Register.DATA, sizeHelper));

        final Typeable rhs = (Typeable)bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        instructions.add(new Pop(Register.COUNTER, sizeHelper));
        instructions.add(new Cmp(Register.DATA, Register.COUNTER, sizeHelper));
        instructions.add(new Pop(Register.COUNTER, sizeHelper));
        
        if(finishEarlyT != null) instructions.add(finishEarlyT.make(end, sizeHelper));
        if(finishEarlyF != null) instructions.add(finishEarlyF.make(endFalse, sizeHelper));

        instructions.add(new Cmp(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
        instructions.add(finishRegular.make(end, sizeHelper));

        instructions.add(new Label(endFalseStr));
        instructions.add(new Xor(Register.BASE, Register.BASE, sizeHelper));
        instructions.add(new Label(endStr));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Comment("End comparison"));
        return instructions;
    }

    @Override
    public final boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        boolean isOk;
        final Typeable ts1 = (Typeable) op.children.get(0);
        final Typeable ts2 = (Typeable) op.children.get(1);
        isOk = ts1.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG) ||
                ts2.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        return isOk;
    }
}
