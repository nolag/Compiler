package cs444.codegen.x86.tiles;

import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.IDivMaker;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class DivTile extends BinUniOpTile<DivideExprSymbol>{
    private DivTile() {
        super(IDivMaker.maker, true);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).divs.add(this);
    }

    public static void init(){
        new DivTile();
    }
}
