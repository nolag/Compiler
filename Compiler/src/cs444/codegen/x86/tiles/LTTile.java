package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.SetlMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.LtExprSymbol;

public class LTTile extends CompOpTile<LtExprSymbol>{
    public static void init(){
        new LTTile();
    }

    private LTTile() {
        super(SetlMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).lts.add(this);
    }

    @Override
    public boolean fits(final LtExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }
}
