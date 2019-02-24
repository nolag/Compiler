package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.BinOpMaker;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class LongBinTile<T extends BinOpExpr> extends LongOnlyTile<X86Instruction, Size, T> {

    private static final Map<Class<? extends BinOpExpr>, LongBinTile> tiles = new HashMap<>();

    private final BinOpMaker first;
    private final BinOpMaker second;
    private final boolean ordered;

    private LongBinTile(BinOpMaker first, BinOpMaker second, boolean ordered) {
        this.first = first;
        this.second = second;
        this.ordered = ordered;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongBinTile<T> getTile(BinOpMaker first, BinOpMaker second,
                                                               boolean ordered,
                                                               Class<T> klass) {
        LongBinTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new LongBinTile(first, second, ordered);
            tiles.put(klass, tile);
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(T bin, Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long add or sub"));
        Typeable lhs = (Typeable) bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.DATA, sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

        Typeable rhs = (Typeable) bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        if (ordered) {
            instructions.add(new Mov(Register.COUNTER, Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
            instructions.add(first.make(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
            instructions.add(new Mov(Register.COUNTER, Register.DATA, sizeHelper));
            instructions.add(new Pop(Register.DATA, sizeHelper));
            instructions.add(second.make(Register.DATA, Register.COUNTER, sizeHelper));
        } else {
            instructions.add(new Pop(Register.COUNTER, sizeHelper));
            instructions.add(first.make(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
            instructions.add(new Pop(Register.COUNTER, sizeHelper));
            instructions.add(second.make(Register.DATA, Register.COUNTER, sizeHelper));
        }

        instructions.add(new Comment("End long bin op"));
        return instructions;
    }
}
