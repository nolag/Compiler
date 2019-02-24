package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.BasicMemoryFormat;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class StaticNameValue extends NumericHelperTile<X86Instruction, Size, SimpleNameSymbol> {
    private static StaticNameValue tile;

    public static StaticNameValue getTile() {
        if (tile == null) {
            tile = new StaticNameValue();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleNameSymbol name, Platform<X86Instruction, Size> platform) {
        return super.fits(name, platform) && name.dcl.isStatic();
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(SimpleNameSymbol name,
                                                          Platform<X86Instruction, Size> platform) {
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        DclSymbol dcl = name.dcl;
        Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));
        String staticFieldLbl = PkgClassResolver.getUniqueNameFor(dcl);

        if (dcl.dclInResolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions
                    .add(new Extern(staticFieldLbl));
        }

        Memory from = new Memory(new BasicMemoryFormat(new Immediate(staticFieldLbl)));
        X86TileHelper.genMov(size, from, dcl.dclName, dcl, sizeHelper, instructions);

        return instructions;
    }
}
