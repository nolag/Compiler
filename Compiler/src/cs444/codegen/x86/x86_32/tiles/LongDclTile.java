package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.DclSymbol;

public class LongDclTile extends LongOnlyTile<DclSymbol>{
    public static void init(){
        new LongDclTile();
    }

    private LongDclTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).dcls.add(this);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final DclSymbol dclSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        if(dclSymbol.children.isEmpty()){
            instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        }else {
            instructions.addAll(platform.getBest(dclSymbol.children.get(0)));
        }
        instructions.add(new Push(Register.DATA, sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        return instructions;
    }
}
