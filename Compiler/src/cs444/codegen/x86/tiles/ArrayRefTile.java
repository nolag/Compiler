package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Add;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.ArrayBaseTile;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public class ArrayRefTile extends ArrayBaseTile{

    public static void init(){
        new ArrayRefTile();
    }

    private ArrayRefTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).arrayRefs.add(this);
    }

    @Override
    public boolean fits(final ArrayAccessExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        instructions.add(new Add(Register.ACCUMULATOR, Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
