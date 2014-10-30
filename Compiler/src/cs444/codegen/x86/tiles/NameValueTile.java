package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.codegen.x86.AddMemoryFormat;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;

public final class NameValueTile extends NumericHelperTile<X86Instruction, Size, SimpleNameSymbol> {
    private static NameValueTile tile;

    public static NameValueTile getTile() {
        if (tile == null) tile = new NameValueTile();
        return tile;
    }

    private NameValueTile() {}

    @Override
    public boolean fits(final SimpleNameSymbol name, final Platform<X86Instruction, Size> platform) {
        return super.fits(name, platform) && !name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name, final Platform<X86Instruction, Size> platform) {

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));

        NotMemory base = dcl.isLocal ? Register.FRAME : Register.ACCUMULATOR;

        final Memory from = new Memory(new AddMemoryFormat(base, new Immediate(dcl.getOffset(platform))));
        X86TileHelper.genMov(size, from, dcl.dclName, dcl, sizeHelper, instructions);

        return instructions;
    }
}
