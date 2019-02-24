package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.Typeable;

public class DclTile extends NumericHelperTile<X86Instruction, Size, DclSymbol> {
    private static DclTile tile;

    private DclTile() {}

    public static DclTile getTile() {
        if (tile == null) {
            tile = new DclTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(DclSymbol dclSymbol, Platform<X86Instruction,
            Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        if (dclSymbol.children.isEmpty()) {
            instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        } else {
            Typeable child = (Typeable) dclSymbol.children.get(0);
            instructions.addAll(platform.getBest(child));
            if (dclSymbol.getType().value.equals(JoosNonTerminal.LONG)) {
                platform.getTileHelper().makeLong(child, instructions, sizeHelper);
            }
        }
        Size size =
                sizeHelper.getPushSize(sizeHelper.getSize(dclSymbol.getType().getTypeDclNode().getRefStackSize(sizeHelper)));
        instructions.add(new Push(Register.ACCUMULATOR, size, sizeHelper));
        return instructions;
    }
}
