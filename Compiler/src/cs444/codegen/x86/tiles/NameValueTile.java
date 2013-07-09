package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class NameValueTile implements ITile<X86Instruction, X86SizeHelper, SimpleNameSymbol>{

    public static void init(){
        new NameValueTile();
    }

    private NameValueTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).nameValues.add(this);
    }

    @Override
    public boolean fits(final SimpleNameSymbol name) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final Size size = sizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        if(dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.getCurrentCodeGen().currentFile) instructions.add(new Extern(staticFieldLbl));

        InstructionArg base = Register.ACCUMULATOR;
        if(dcl.isLocal) base = Register.FRAME;
        else if(dcl.isStatic()) base = new Immediate(staticFieldLbl);

        final InstructionArg from = new Memory(base, dcl.getOffset());
        X86TileHelper.genMov(size, from, dcl.dclName, dcl, sizeHelper, instructions);

        return instructions;
    }
}
