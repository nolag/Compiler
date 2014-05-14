package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.BasicMemoryFormat;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericHelperTile;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class StaticNameValue extends NumericHelperTile<SimpleNameSymbol> {
    private static StaticNameValue tile;

    public static void init(){
        if(tile == null) tile = new StaticNameValue();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).nameValues.add(tile);
    }

    @Override
    public boolean fits(final SimpleNameSymbol name, final Platform<X86Instruction, Size> platform) {
        return super.fits(name, platform) && name.dcl.isStatic();
    }
    
    @Override
    public InstructionsAndTiming<X86Instruction> generate(SimpleNameSymbol name, Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));
        final String staticFieldLbl = PkgClassResolver.getUniqueNameFor(dcl);
        
        if(dcl.dclInResolver != CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile) instructions.add(new Extern(staticFieldLbl));
        
        final Memory from = new Memory(new BasicMemoryFormat(new Immediate(staticFieldLbl)));
        X86TileHelper.genMov(size, from, dcl.dclName, dcl, sizeHelper, instructions);

        return instructions;
    }
}
