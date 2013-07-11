package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.SeteMaker;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.tiles.helpers.CompOpTile;
import cs444.parser.symbols.ast.expressions.EqExprSymbol;

public class EQTile extends CompOpTile<EqExprSymbol>{
    public static void init(){
        new EQTile();
    }

    private EQTile() {
        super(SeteMaker.maker);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).eqs.add(this);
    }

    @Override
    public boolean fits(final EqExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }
}
