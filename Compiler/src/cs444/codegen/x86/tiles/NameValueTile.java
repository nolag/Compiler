package cs444.codegen.x86.tiles;

import cs444.codegen.IPlatform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class NameValueTile implements ITile<X86Instruction, SimpleNameSymbol>{
    public static final NameValueTile instance = new NameValueTile();

    private NameValueTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).nameValues.add(this);
    }

    @Override
    public boolean fits(final SimpleNameSymbol name) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name, final IPlatform<X86Instruction> platform) {
        final X86SizeHelper sizeHelper = (X86SizeHelper)platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final Size size = X86SizeHelper.getSize(dcl.getType().getTypeDclNode().getRealSize(sizeHelper));
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        InstructionArg base = Register.ACCUMULATOR;
        if(dcl.isLocal) base = Register.FRAME;
        else if(dcl.isStatic()) base = new Immediate(staticFieldLbl);

        final InstructionArg from = new Memory(base, dcl.getOffset());
        TileHelper.genMov(size, from, dcl.dclName, dcl, instructions, sizeHelper);

        return instructions;
    }

    @Override
    public boolean isBaseTile() {
        return true;
    }
}
