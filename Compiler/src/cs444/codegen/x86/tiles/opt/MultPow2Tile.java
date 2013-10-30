package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Shl;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class MultPow2Tile implements ITile<X86Instruction, Size, MultiplyExprSymbol> {
    private static MultPow2Tile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new MultPow2Tile();
        TileSet.<X86Instruction, Size>getOrMake(klass).mults.add(tile);
    }

    @Override
    public boolean fits(final MultiplyExprSymbol op, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final boolean canUes = TileHelper.powerTwoOrNull(op.children.get(0))  != null|| TileHelper.powerTwoOrNull(op.children.get(1)) != null;
        return canUes && sizeHelper.getDefaultStackSize()  >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final MultiplyExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<>();
        final ISymbol first = symbol.children.get(0);
        final ISymbol second = symbol.children.get(1);
        Integer value = TileHelper.powerTwoOrNull(first);
        ISymbol other = second;

        if (value == null) {
            value = TileHelper.powerTwoOrNull(second);
            other = first;
        }

        instructions.addAll(platform.getBest(other));
        instructions.add(new Shl(Register.ACCUMULATOR, new Immediate((long)value), platform.getSizeHelper()));

        return instructions;
    }
}
