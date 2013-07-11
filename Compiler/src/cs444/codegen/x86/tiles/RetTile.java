package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Leave;
import cs444.codegen.instructions.x86.Ret;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;

public class RetTile implements ITile<X86Instruction, Size, ReturnExprSymbol> {
    public static void init(){
        new RetTile();
    }

    private RetTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).rets.add(this);
    }


    @Override
    public boolean fits(final ReturnExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ReturnExprSymbol retSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        if(retSymbol.children.size() == 1) instructions.addAll(platform.getBest(retSymbol.children.get(0)));
        //value is already in eax from the rule therefore we just need to ret
        instructions.add(Leave.LEAVE);
        instructions.add(Ret.RET);
        return instructions;
    }

}
