package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericHelperTile;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class NameValueTile extends NumericHelperTile<SimpleNameSymbol>{
    private static NameValueTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new NameValueTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).nameValues.add(tile);
    }

    private NameValueTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name,
            final Platform<X86Instruction, Size> platform) {

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        if(dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile) instructions.add(new Extern(staticFieldLbl));

        InstructionArg base = Register.ACCUMULATOR;
        if(dcl.isLocal) base = Register.FRAME;
        else if(dcl.isStatic()) base = new Immediate(staticFieldLbl);

        final Memory from = new Memory(base, dcl.getOffset(platform));
        X86TileHelper.genMov(size, from, dcl.dclName, dcl, sizeHelper, instructions);

        return instructions;
    }
}
