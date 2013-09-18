package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Cdq;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.RSOpMaker;
import cs444.codegen.x86.instructions.factories.ShrdMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongShiftTile;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class LongRigthShiftTile extends LongShiftTile<RSExprSymbol> {
    private static LongRigthShiftTile tile;

    public static void init() {
        if(tile == null) tile = new LongRigthShiftTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).rss.add(tile);
    }

    private LongRigthShiftTile() {
        super(RSOpMaker.maker, ShrdMaker.maker, true);
    }

    @Override
    protected X86Instruction bigFinish(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return Cdq.cdq;
    }
}
