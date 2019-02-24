package cs444.codegen.x86.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Shl;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class MultPow2Tile implements ITile<X86Instruction, Size, MultiplyExprSymbol> {
    private static MultPow2Tile tile;

    public static MultPow2Tile getTile() {
        if (tile == null) {
            tile = new MultPow2Tile();
        }
        return tile;
    }

    @Override
    public boolean fits(MultiplyExprSymbol op, Platform<X86Instruction, Size> platform) {
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        boolean canUes =
                TileHelper.powerTwoOrNull(op.children.get(0)) != null || TileHelper.powerTwoOrNull(op.children.get(1)) != null;
        return canUes && sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(op.getType().getTypeDclNode().fullName);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(MultiplyExprSymbol symbol,
                                                          Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<>();
        ISymbol first = symbol.children.get(0);
        ISymbol second = symbol.children.get(1);
        Integer value = TileHelper.powerTwoOrNull(first);
        ISymbol other = second;

        if (value == null) {
            value = TileHelper.powerTwoOrNull(second);
            other = first;
        }

        instructions.addAll(platform.getBest(other));
        instructions.add(new Shl(Register.ACCUMULATOR, new Immediate((long) value), platform.getSizeHelper()));

        return instructions;
    }
}
