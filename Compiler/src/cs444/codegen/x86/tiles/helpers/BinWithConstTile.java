package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.ILiteralSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class BinWithConstTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T> {
    public static enum Side {
        EITHER,
        RIGHT,
        LEFT
    }
    
    private final BinOpMaker maker;
    private final Side side;

    protected BinWithConstTile(final BinOpMaker maker) {
        this.maker = maker;
        this.side = Side.EITHER;
    }
    
    protected BinWithConstTile(final BinOpMaker maker, Side side) {
        this.maker = maker;
        this.side = side;
    }
    
    private boolean useable(Typeable t) {
        if (!(t instanceof ILiteralSymbol)) return false;
        long lval = ((ILiteralSymbol)t).getAsLongValue();
        int ival = (int) lval;
        return  lval == ival;
    }
    
    @Override
    public boolean fits(final T bin, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        if (sizeHelper.getDefaultStackSize() < sizeHelper.getByteSizeOfType(bin.getType().getTypeDclNode().fullName)) return false;
        final Typeable t1 = (Typeable)bin.children.get(0);
        final Typeable t2 = (Typeable)bin.children.get(1);
        //Since instructions other than mov don't support 64 bit literals
        switch (side) {
        case EITHER:
            return useable(t1) || useable(t2);
        case LEFT:
            return useable(t1);
        case RIGHT:
            return useable(t2);
        default:
            return false;
        }
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(T bin, Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final Typeable t1 = (Typeable)bin.children.get(0);
        final Typeable t2 = (Typeable)bin.children.get(1);

        final TypeSymbol ts1 = t1.getType();
        final TypeSymbol ts2 = t2.getType();

        final boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG) ||
                ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);
       
        
        final ILiteralSymbol literal;
        final Typeable other;
        if(t1 instanceof ILiteralSymbol) {
            literal = (ILiteralSymbol) t1;
            other = t2;
        } else {
            literal = (ILiteralSymbol) t2;
            other = t1;
        }
        
        instructions.addAll(platform.getBest(other));
        final Size size;

        if(hasLong) {
            size = Size.QWORD;
            platform.getTileHelper().makeLong(other, instructions, sizeHelper);
        }else{
            size = Size.DWORD;
        }

        instructions.add(maker.make(Register.ACCUMULATOR, new Immediate(literal.getAsLongValue()), size, sizeHelper));
        return instructions;
    }
}
