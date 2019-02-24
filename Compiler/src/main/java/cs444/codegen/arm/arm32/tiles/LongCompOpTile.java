package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class LongCompOpTile<T extends BinOpExpr> implements ITile<ArmInstruction, Size, T> {
    private static final Map<Class<? extends BinOpExpr>, LongCompOpTile> tiles = new HashMap<>();

    private final Condition finishEarlyT;
    private final Condition finishEarlyF;
    private final Condition finishRegular;

    private LongCompOpTile(Condition finishEarlyT, Condition finishEarlyF, Condition finishRegular) {
        this.finishEarlyT = finishEarlyT;
        this.finishEarlyF = finishEarlyF;
        this.finishRegular = finishRegular;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongCompOpTile<T> getTile(Condition finishEarlyT,
                                                                  Condition finishEarlyF,
                                                                  Condition finishRegular, Class<T> klass) {
        LongCompOpTile<T> tile = tiles.get(klass);
        if (tile == null) {
            tile = new LongCompOpTile(finishEarlyT, finishEarlyF, finishRegular);
            tiles.put(klass, tile);
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(T bin, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();
        long endNum = CodeGenVisitor.getNewLblNum();
        String end = "cmpEnd" + endNum;
        String endFalse = "cmpFalse" + endNum;

        instructions.add(new Comment("Start comparison "));

        instructions.add(new Push(Register.R8));
        instructions.add(new Mov(Register.R8, Immediate8.TRUE, sizeHelper));

        Typeable lhs = (Typeable) bin.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        // Combining pushes 2 first.  We need to push R2 after R0
        instructions.add(new Push(Register.R0));
        instructions.add(new Push(Register.R2));

        Typeable rhs = (Typeable) bin.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        instructions.add(new Pop(Register.R1));
        instructions.add(new Cmp(Register.R1, Register.R2, sizeHelper));
        instructions.add(new Pop(Register.R1));

        if (finishEarlyT != null) {
            instructions.add(new B(finishEarlyT, end));
        }
        if (finishEarlyF != null) {
            instructions.add(new B(finishEarlyF, endFalse));
        }

        instructions.add(new Cmp(Register.R1, Register.R0, sizeHelper));
        instructions.add(new B(finishRegular, end));

        instructions.add(new Label(endFalse));
        instructions.add(new Eor(Register.R8, Register.R8, Register.R8, sizeHelper));
        instructions.add(new Label(end));
        instructions.add(new Mov(Register.R0, Register.R8, sizeHelper));

        instructions.add(new Pop(Register.R8));
        instructions.add(new Comment("End comparison"));
        return instructions;
    }

    @Override
    public final boolean fits(T op, Platform<ArmInstruction, Size> platform) {
        boolean isOk;
        Typeable ts1 = (Typeable) op.children.get(0);
        Typeable ts2 = (Typeable) op.children.get(1);
        isOk = ts1.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)
                || ts2.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);

        return isOk;
    }
}
