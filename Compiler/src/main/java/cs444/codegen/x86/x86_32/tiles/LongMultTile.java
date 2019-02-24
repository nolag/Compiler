package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class LongMultTile extends LongOnlyTile<X86Instruction, Size, MultiplyExprSymbol> {
    private static LongMultTile tile;

    private LongMultTile() {}

    public static LongMultTile getTile() {
        if (tile == null) {
            tile = new LongMultTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(MultiplyExprSymbol mult,
                                                          Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long mult"));

        instructions.add(new Push(Register.BASE, sizeHelper));

        Typeable lhs = (Typeable) mult.children.get(0);
        Typeable rhs = (Typeable) mult.children.get(1);

        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Push(Register.DATA, sizeHelper));

        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.COUNTER, Register.DATA, sizeHelper));
        instructions.add(new Pop(Register.DATA, sizeHelper));
        instructions.add(new IMul(Register.DATA, sizeHelper));
        instructions.add(new Mov(Register.SOURCE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
        instructions.add(new Pop(Register.COUNTER, sizeHelper));
        instructions.add(new IMul(Register.COUNTER, sizeHelper));
        instructions.add(new Xchg(Register.ACCUMULATOR, Register.BASE, sizeHelper));
        instructions.add(new Mul(Register.COUNTER, sizeHelper));
        instructions.add(new Add(Register.DATA, Register.BASE, sizeHelper));
        instructions.add(new Add(Register.DATA, Register.SOURCE, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Comment("End long mult"));

        return instructions;
    }
}