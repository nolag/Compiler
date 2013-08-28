package cs444.codegen.x86.x86_32.tiles.helpers;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.codegen.x86.instructions.factories.ShXdMaker;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class LongShiftTile<T extends BinOpExpr> extends LongOnlyTile<T>{
    private final BinOpMaker shift;
    private final ShXdMaker shiftCpy;
    private final boolean aFirst;

    protected LongShiftTile(final BinOpMaker shift, final ShXdMaker shiftCpy, final boolean aFirst){
        this.shift = shift;
        this.shiftCpy = shiftCpy;
        this.aFirst = aFirst;
    }

    protected abstract X86Instruction bigFinish(final SizeHelper<X86Instruction, Size> sizeHelper);

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T symbol, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final ISymbol num = symbol.children.get(0);
        final ISymbol shiftBy = symbol.children.get(1);

        instructions.add(new Comment("Long shift start"));
        instructions.addAll(platform.getBest(num));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Push(Register.DATA, sizeHelper));

        instructions.addAll(platform.getBest(shiftBy));
        instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));

        instructions.add(new Pop(Register.DATA, sizeHelper));
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        Register r1, r2;
        if(aFirst){
            r1 = Register.ACCUMULATOR;
            r2 = Register.DATA;
        }else{
            r1 = Register.DATA;
            r2 = Register.ACCUMULATOR;
        }

        final long myVal = CodeGenVisitor.getNewLblNum();

        final String big = "big" + myVal;
        final String end = "done" + myVal;

        instructions.add(new Comment("making -ve numbers work"));
        instructions.add(new And(Register.COUNTER, Immediate.SIXTY_THREE, sizeHelper));
        instructions.add(new Comment("Checking if this number is biger than 31!"));
        instructions.add(new Cmp(Register.COUNTER, Immediate.THIRTY_ONE, sizeHelper));
        instructions.add(new Jge(new Immediate(big), sizeHelper));

        instructions.add(new Comment("number was smaller than 31"));
        instructions.add(shiftCpy.make(r1, r2, Register.COUNTER, sizeHelper));
        instructions.add(shift.make(r2, Register.COUNTER, sizeHelper));
        instructions.add(new Jmp(new Immediate(end), sizeHelper));

        instructions.add(new Comment("number was big"));
        instructions.add(new Label(big));
        instructions.add(new Mov(r1, r2, sizeHelper));
        instructions.add(new Sub(Register.COUNTER, Immediate.THIRTY_TWO, sizeHelper));
        instructions.add(shift.make(r1, Register.COUNTER, sizeHelper));
        instructions.add(bigFinish(sizeHelper));

        instructions.add(new Label(end));
        return instructions;
    }
}
