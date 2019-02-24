package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.ArrayBaseTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class LongArrayValueTile extends ArrayBaseTile {
    private static final MemoryFormat accBase = new AddMemoryFormat(Register.ACCUMULATOR, Register.BASE);
    private static final MemoryFormat accBaseFour = new AddMemoryFormat(accBase, Immediate.FOUR);
    private static LongArrayValueTile tile;

    private LongArrayValueTile() {}

    public static LongArrayValueTile getTile() {
        if (tile == null) {
            tile = new LongArrayValueTile();
        }
        return tile;
    }

    @Override
    public final boolean fits(ArrayAccessExprSymbol typeable, Platform<X86Instruction, Size> platform) {
        return typeable.getType().value.equals(JoosNonTerminal.LONG);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(ArrayAccessExprSymbol arrayAccess,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        Memory meml = new Memory(accBase);
        Memory memh = new Memory(accBaseFour);
        instructions.add(new Mov(Register.DATA, memh, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, meml, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
