package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;
import cs444.parser.symbols.ast.DclSymbol;

public class DclTile implements ITile<X86Instruction, Size, DclSymbol>{
    public static void init(){
        new DclTile();
    }

    private DclTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).dcls.add(this);
    }

    @Override
    public boolean fits(final DclSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final DclSymbol dclSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        if(dclSymbol.children.isEmpty())instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        else instructions.addAll(platform.getBest(dclSymbol.children.get(0)));
        final Size size = sizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, size, sizeHelper));
        return instructions;
    }
}
