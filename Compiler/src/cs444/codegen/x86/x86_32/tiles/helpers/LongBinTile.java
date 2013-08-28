package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class LongBinTile<T extends BinOpExpr> extends  LongOnlyTile<T>{
    private final BinOpMaker first;
    private final BinOpMaker second;
    private final boolean ordered;

    protected LongBinTile(final BinOpMaker first, final BinOpMaker second, final boolean ordered){
        this.first = first;
        this.second = second;
        this.ordered = ordered;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long add or sub"));
        final Typeable lhs = (Typeable)bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.DATA, sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));


        final Typeable rhs = (Typeable)bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        if(ordered){
            instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
            instructions.add(first.make(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
            instructions.add(new Mov(Register.COUNTER, Register.DATA, sizeHelper));
            instructions.add(new Pop(Register.DATA, sizeHelper));
            instructions.add(second.make(Register.DATA, Register.COUNTER, sizeHelper));
        }else{
            instructions.add(new Pop(Register.COUNTER, sizeHelper));
            instructions.add(first.make(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
            instructions.add(new Pop(Register.COUNTER, sizeHelper));
            instructions.add(second.make(Register.DATA, Register.COUNTER, sizeHelper));
        }

        instructions.add(new Comment("End long add or sub"));
        return instructions;
    }
}
