package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;

public final class NameValueTile extends NumericHelperTile<X86Instruction, Size, SimpleNameSymbol> {
    private static NameValueTile tile;

    private NameValueTile() {}

    public static NameValueTile getTile() {
        if (tile == null) {
            tile = new NameValueTile();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleNameSymbol name, Platform<X86Instruction, Size> platform) {
        return super.fits(name, platform) && !name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(SimpleNameSymbol name, Platform<X86Instruction,
            Size> platform) {

        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<>();
        DclSymbol dcl = name.dcl;
        Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));

        NotMemory base = dcl.isLocal ? Register.FRAME : Register.ACCUMULATOR;

        Memory from = new Memory(new AddMemoryFormat(base, new Immediate(dcl.getOffset(platform))));
        X86TileHelper.genMov(size, from, dcl.dclName, dcl, sizeHelper, instructions);

        return instructions;
    }
}
