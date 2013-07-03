package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.Xor;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.DclSymbol;

public class DclTile implements ITile<X86Instruction, DclSymbol>{
    public static void init(){
        new DclTile();
    }

    private DclTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).dcls.add(this);
    }

    @Override
    public boolean fits(final DclSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final DclSymbol dclSymbol, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper)platform.getSizeHelper();
        if(dclSymbol.children.isEmpty())instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        else instructions.addAll(platform.getBest(dclSymbol.children.get(0)));
        final Size size = X86SizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, size, sizeHelper));
        return instructions;
    }
}
