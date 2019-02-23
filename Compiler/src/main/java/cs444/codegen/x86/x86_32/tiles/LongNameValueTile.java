package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.AddMemoryFormat;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class LongNameValueTile extends LongOnlyTile<X86Instruction, Size, SimpleNameSymbol> {
    private static LongNameValueTile tile;

    public static LongNameValueTile getTile() {
        if (tile == null) tile = new LongNameValueTile();
        return tile;
    }

    private LongNameValueTile() {}

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name, final Platform<X86Instruction, Size> platform) {

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        if (dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.<X86Instruction, Size> getCurrentCodeGen(platform).currentFile) instructions
                .add(new Extern(staticFieldLbl));

        NotMemory base = Register.ACCUMULATOR;
        if (dcl.isLocal) base = Register.FRAME;
        else if (dcl.isStatic()) base = new Immediate(staticFieldLbl);

        final Memory fromh = new Memory(new AddMemoryFormat(base, new Immediate(dcl.getOffset(platform) + 4)));
        final Memory froml = new Memory(new AddMemoryFormat(base, new Immediate(dcl.getOffset(platform))));
        instructions.add(new Mov(Register.DATA, fromh, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, froml, sizeHelper));

        return instructions;
    }
}
