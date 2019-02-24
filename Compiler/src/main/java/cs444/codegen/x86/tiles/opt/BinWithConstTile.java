package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.ILiteralSymbol;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BinWithConstTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T> {
    private static final Map<Side, Map<Class<? extends BinOpExpr>, BinWithConstTile>> tiles = new EnumMap<>(Side.class);
    private final BinOpMaker maker;
    private final Side side;

    protected BinWithConstTile(BinOpMaker maker) {
        this.maker = maker;
        side = Side.EITHER;
    }

    protected BinWithConstTile(BinOpMaker maker, Side side) {
        this.maker = maker;
        this.side = side;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> BinWithConstTile<T> getTile(BinOpMaker maker, Side side,
                                                                    Class<T> klass) {
        Map<Class<? extends BinOpExpr>, BinWithConstTile> binMap = tiles.get(side);
        if (binMap == null) {
            tiles.put(side, binMap = new HashMap<>());
        }
        BinWithConstTile<T> tile = binMap.get(klass);
        if (tile == null) {
            tile = new BinWithConstTile(maker, side);
            binMap.put(klass, tile);
        }
        return tile;
    }

    private boolean useable(Typeable t) {
        if (!(t instanceof ILiteralSymbol)) {
            return false;
        }
        long lval = ((ILiteralSymbol) t).getAsLongValue();
        int ival = (int) lval;
        return lval == ival;
    }

    @Override
    public boolean fits(T bin, Platform<X86Instruction, Size> platform) {
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        if (sizeHelper.getDefaultStackSize() < sizeHelper.getByteSizeOfType(bin.getType().getTypeDclNode().fullName)) {
            return false;
        }
        Typeable t1 = (Typeable) bin.children.get(0);
        Typeable t2 = (Typeable) bin.children.get(1);
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
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        Typeable t1 = (Typeable) bin.children.get(0);
        Typeable t2 = (Typeable) bin.children.get(1);

        TypeSymbol ts1 = t1.getType();
        TypeSymbol ts2 = t2.getType();

        boolean hasLong = ts1.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        ILiteralSymbol literal;
        Typeable other;
        if (t1 instanceof ILiteralSymbol) {
            literal = (ILiteralSymbol) t1;
            other = t2;
        } else {
            literal = (ILiteralSymbol) t2;
            other = t1;
        }

        instructions.addAll(platform.getBest(other));
        Size size;

        if (hasLong) {
            size = Size.QWORD;
            platform.getTileHelper().makeLong(other, instructions, sizeHelper);
        } else {
            size = Size.DWORD;
        }

        instructions.add(maker.make(Register.ACCUMULATOR, new Immediate(literal.getAsLongValue()), size, sizeHelper));
        return instructions;
    }

    public enum Side {
        EITHER, RIGHT, LEFT
    }
}
