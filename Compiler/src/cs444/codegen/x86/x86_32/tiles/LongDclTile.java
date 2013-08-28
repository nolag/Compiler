package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.Typeable;

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
        final TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();

        if(dclSymbol.children.isEmpty()){
            final X86Instruction push0 = new Push(Immediate.ZERO, sizeHelper);
            instructions.add(push0);
            instructions.add(push0);
        }else {
            final Typeable init = (Typeable)dclSymbol.children.get(0);
            instructions.addAll(platform.getBest(init));
            tileHelper.pushLong(init, instructions, sizeHelper);
        }

        return instructions;
    }
}
