package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.UniOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinUniOpTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T> {
    private final UniOpMaker maker;
    private final boolean sar;

    protected BinUniOpTile(final UniOpMaker maker, final boolean sar){
        this.maker = maker;
        this.sar = sar;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final IRuntime<X86Instruction> runtime = platform.getRunime();

        final Typeable t1 = (Typeable)bin.children.get(0);
        final Typeable t2 = (Typeable)bin.children.get(0);

        final TypeSymbol ts1 = t1.getType();
        final TypeSymbol ts2 = t2.getType();

        final boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG) ||
                ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.addAll(platform.getBest(bin.children.get(0)));

        final Size size;

        if(hasLong) {
            size = Size.QWORD;
            platform.getTileHelper().makeLong(t1, instructions, sizeHelper);
        }else{
            size = Size.DWORD;
        }

        if(hasLong) platform.getTileHelper().makeLong(t1, instructions, sizeHelper);

        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(bin.children.get(1)));

        if(hasLong) platform.getTileHelper().makeLong(t2, instructions, sizeHelper);

        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        // pop first operand
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));

        // first operand -> eax, second operand -> ebx
        if(sar){
            final Immediate shift = hasLong ? Immediate.SIXTY_THREE : Immediate.THIRTY_ONE;
            instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, size, sizeHelper));
            instructions.add(new Sar(Register.DATA, shift, size, sizeHelper));
            final String safeDiv = "safeDiv" + CodeGenVisitor.getNewLblNum();
            X86TileHelper.setupJumpNe(Register.BASE, Immediate.ZERO, safeDiv, sizeHelper, instructions, size);
            runtime.throwException(instructions, JoosNonTerminal.DIV_ZERO);
            instructions.add(new Label(safeDiv));
        }

        instructions.add(maker.make(Register.BASE, sizeHelper, size));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }


    @Override
    public final boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize()  >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }
}
