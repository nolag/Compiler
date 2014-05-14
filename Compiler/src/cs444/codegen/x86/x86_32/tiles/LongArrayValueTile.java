package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.ArrayBaseTile;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class LongArrayValueTile extends ArrayBaseTile {
    private static LongArrayValueTile tile;

    private static final MemoryFormat accBase = new AddMemoryFormat(Register.ACCUMULATOR, Register.BASE);
    private static final MemoryFormat accBaseFour = new AddMemoryFormat(accBase, Immediate.FOUR);

    public static void init() {
        if(tile == null) tile = new LongArrayValueTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).arrayValues.add(tile);
    }

    private LongArrayValueTile() { }

    @Override
    public final boolean fits(final ArrayAccessExprSymbol typeable, final Platform<X86Instruction, Size> platform) {
        return typeable.getType().value.equals(JoosNonTerminal.LONG);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final Memory meml = new Memory(accBase);
        final Memory memh = new Memory(accBaseFour);
        instructions.add(new Mov(Register.DATA, memh, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, meml, sizeHelper));

        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
