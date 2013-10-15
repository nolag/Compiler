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
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class LongNameValueTile extends LongOnlyTile<SimpleNameSymbol> {
    private static LongNameValueTile tile;

    public static void init() {
        if(tile == null) tile = new LongNameValueTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).nameValues.add(tile);
    }

    private LongNameValueTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name,
            final Platform<X86Instruction, Size> platform) {

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        if(dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile)
            instructions.add(new Extern(staticFieldLbl));

        InstructionArg base = Register.ACCUMULATOR;
        if(dcl.isLocal) base = Register.FRAME;
        else if(dcl.isStatic()) base = new Immediate(staticFieldLbl);

        final Memory fromh = new Memory(base, dcl.getOffset(platform) + 4);
        final Memory froml = new Memory(base, dcl.getOffset(platform));
        instructions.add(new Mov(Register.DATA, fromh, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, froml, sizeHelper));

        return instructions;
    }
}
