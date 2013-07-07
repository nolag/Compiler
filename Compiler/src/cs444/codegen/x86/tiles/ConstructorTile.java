package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Call;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.types.APkgClassResolver;

public class ConstructorTile implements ITile<X86Instruction, X86SizeHelper, ConstructorSymbol>{
    public static void init(){
        new ConstructorTile();
    }

    private ConstructorTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).constructors.add(this);
    }

    @Override
    public boolean fits(final ConstructorSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ConstructorSymbol constructor,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        final String constrName = APkgClassResolver.generateFullId(constructor);
        final X86SizeHelper sizeHelper = platform.getSizeHelper();

        TileHelper.methProlog(constructor, constrName, sizeHelper, instructions);

        instructions.add(new Mov(Register.ACCUMULATOR, Memory.getThisPointer(sizeHelper), sizeHelper));

        instructions.add(new Call(new Immediate(CodeGenVisitor.INIT_OBJECT_FUNC), sizeHelper));

        for(final ISymbol child : constructor.children) instructions.addAll(platform.getBest(child));

        TileHelper.methEpilogue(constructor, instructions);
        return instructions;
    }

}
