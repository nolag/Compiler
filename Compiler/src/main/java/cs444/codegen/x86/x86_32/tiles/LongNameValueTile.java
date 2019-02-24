package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public final class LongNameValueTile extends LongOnlyTile<X86Instruction, Size, SimpleNameSymbol> {
    private static LongNameValueTile tile;

    private LongNameValueTile() {}

    public static LongNameValueTile getTile() {
        if (tile == null) {
            tile = new LongNameValueTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(SimpleNameSymbol name, Platform<X86Instruction,
            Size> platform) {

        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        DclSymbol dcl = name.dcl;
        String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;

        if (dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions
                    .add(new Extern(staticFieldLbl));
        }

        NotMemory base = Register.ACCUMULATOR;
        if (dcl.isLocal) {
            base = Register.FRAME;
        } else if (dcl.isStatic()) {
            base = new Immediate(staticFieldLbl);
        }

        Memory fromh = new Memory(new AddMemoryFormat(base, new Immediate(dcl.getOffset(platform) + 4)));
        Memory froml = new Memory(new AddMemoryFormat(base, new Immediate(dcl.getOffset(platform))));
        instructions.add(new Mov(Register.DATA, fromh, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, froml, sizeHelper));

        return instructions;
    }
}
