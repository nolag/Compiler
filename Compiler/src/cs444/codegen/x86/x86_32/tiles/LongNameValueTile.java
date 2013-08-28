package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class LongNameValueTile extends LongOnlyTile<SimpleNameSymbol>{

    public static void init(){
        new LongNameValueTile();
    }

    private LongNameValueTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).nameValues.add(this);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name,
            final Platform<X86Instruction, Size> platform) {

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        if(dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile) instructions.add(new Extern(staticFieldLbl));

        InstructionArg base = Register.ACCUMULATOR;
        if(dcl.isLocal) base = Register.FRAME;
        else if(dcl.isStatic()) base = new Immediate(staticFieldLbl);

        final InstructionArg fromh = new Memory(base, dcl.getOffset() + 4);
        final InstructionArg froml = new Memory(base, dcl.getOffset());
        instructions.add(new Mov(Register.DATA, fromh, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, froml, sizeHelper));

        return instructions;
    }
}
