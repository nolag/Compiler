package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class NameRefTile implements ITile<X86Instruction, Size, SimpleNameSymbol> {
    private static NameRefTile tile;

    public static NameRefTile getTile() {
        if(tile == null) tile = new NameRefTile();
        return tile;
    }

    private NameRefTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Move reference of " + dcl.dclName + " in to Accumulator"));
        if(dcl.isStatic()){
            if (dcl.dclInResolver != CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile) {
                instructions.add(new Extern(staticFieldLbl));
            }
            instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(staticFieldLbl), sizeHelper));
        }else if(dcl.isLocal){
            instructions.add(new Comment("mov frame to accumulator because it is local"));
            instructions.add(new Lea(Register.ACCUMULATOR, new Memory(new AddMemoryFormat(Register.FRAME, new Immediate(dcl.getOffset(platform)))), sizeHelper));
        } else {
            instructions.add(new Add(Register.ACCUMULATOR, new Immediate(dcl.getOffset(platform)), sizeHelper));
        }

        return instructions;
    }

    @Override
    public boolean fits(final SimpleNameSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }
}
