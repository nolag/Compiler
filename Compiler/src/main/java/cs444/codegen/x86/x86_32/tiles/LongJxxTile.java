package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.JxxMaker;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class LongJxxTile<T extends BinOpExpr> implements ITile<X86Instruction, Size, T> {
    private static final Map<Class<? extends BinOpExpr>, LongJxxTile> tiles = new HashMap<>();

    private final JxxMaker finishEarlyT;
    private final JxxMaker finishEarlyF;
    private final JxxMaker finishRegular;

    private LongJxxTile(JxxMaker finishEarlyT, JxxMaker finishEarlyF, JxxMaker finishRegular) {
        this.finishEarlyT = finishEarlyT;
        this.finishEarlyF = finishEarlyF;
        this.finishRegular = finishRegular;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongJxxTile<T> getTile(JxxMaker finishEarlyT, JxxMaker finishEarlyF,
                                                               JxxMaker finishRegular, Class<T> klass) {
        LongJxxTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new LongJxxTile(finishEarlyT, finishEarlyF, finishRegular);
            tiles.put(klass, tile);
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(T bin, Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();
        long endNum = CodeGenVisitor.getNewLblNum();
        String endStr = "cmpEnd" + endNum;
        String endFalseStr = "cmpFalse" + endNum;
        Immediate end = new Immediate(endStr);
        Immediate endFalse = new Immediate(endFalseStr);

        instructions.add(new Comment("Start comparison "));

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Immediate.TRUE, sizeHelper));

        Typeable lhs = (Typeable) bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Push(Register.DATA, sizeHelper));

        Typeable rhs = (Typeable) bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        instructions.add(new Pop(Register.COUNTER, sizeHelper));
        instructions.add(new Cmp(Register.DATA, Register.COUNTER, sizeHelper));
        instructions.add(new Pop(Register.COUNTER, sizeHelper));

        if (finishEarlyT != null) {
            instructions.add(finishEarlyT.make(end, sizeHelper));
        }
        if (finishEarlyF != null) {
            instructions.add(finishEarlyF.make(endFalse, sizeHelper));
        }

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
    public final boolean fits(T op, Platform<X86Instruction, Size> platform) {
        boolean isOk;
        Typeable ts1 = (Typeable) op.children.get(0);
        Typeable ts2 = (Typeable) op.children.get(1);
        isOk = ts1.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        return isOk;
    }
}
