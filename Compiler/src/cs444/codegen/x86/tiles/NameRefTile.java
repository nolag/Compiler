package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Add;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class NameRefTile implements ITile<X86Instruction, X86SizeHelper, SimpleNameSymbol>{

    public static void init(){
        new NameRefTile();
    }

    private NameRefTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).nameRefs.add(this);
    }

    @Override
    public boolean fits(final SimpleNameSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleNameSymbol name,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        if(dcl.isStatic() && dcl.dclInResolver != CodeGenVisitor.getCurrentCodeGen().currentFile) instructions.add(new Extern(staticFieldLbl));
        X86Instruction instruction = new Add(Register.ACCUMULATOR, new Immediate(dcl.getOffset()), sizeHelper);
        if(dcl.isStatic()){
            instruction = new Mov(Register.ACCUMULATOR, new Immediate(staticFieldLbl), sizeHelper);
        }else if(dcl.isLocal){
            instructions.add(new Comment("mov frame to accumulator because it is local"));
            instructions.add(new Mov(Register.ACCUMULATOR, Register.FRAME, sizeHelper));
        }

        instructions.add(new Comment("Move reference of " + dcl.dclName + " in to Accumulator"));
        instructions.add(instruction);
        return instructions;
    }
}
