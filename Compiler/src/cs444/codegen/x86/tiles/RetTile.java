package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Leave;
import cs444.codegen.x86.instructions.Ret;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.ReturnExprSymbol;

public class RetTile implements ITile<X86Instruction, Size, ReturnExprSymbol> {
    private static RetTile tile;
    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new RetTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).rets.add(tile);
    }

    private RetTile() { }


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
