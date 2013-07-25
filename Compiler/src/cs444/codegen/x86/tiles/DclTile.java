package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericHelperTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.Typeable;

public class DclTile extends NumericHelperTile<DclSymbol>{
    public static void init(){
        new DclTile();
    }

    private DclTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).dcls.add(this);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final DclSymbol dclSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        if(dclSymbol.children.isEmpty()){
            instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        }
        else {
            final Typeable child = (Typeable)dclSymbol.children.get(0);
            instructions.addAll(platform.getBest(child));
            if(dclSymbol.getType().equals(JoosNonTerminal.LONG)) platform.getTileHelper().makeLong(child, instructions, sizeHelper);
        }
        final Size size = sizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper));
        instructions.add(new Push(Register.ACCUMULATOR, size, sizeHelper));
        return instructions;
    }
}
