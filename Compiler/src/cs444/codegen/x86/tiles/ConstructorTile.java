package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Call;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.types.APkgClassResolver;

public class ConstructorTile implements ITile<X86Instruction, Size, ConstructorSymbol> {
    private static ConstructorTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if (tile == null) tile = new ConstructorTile();
        TileSet.<X86Instruction, Size> getOrMake(klass).constructors.add(tile);
    }

    private ConstructorTile() {}

    @Override
    public boolean fits(final ConstructorSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ConstructorSymbol constructor, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        final String constrName = APkgClassResolver.generateFullId(constructor);
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        platform.getTileHelper().methProlog(constructor, constrName, sizeHelper, instructions);

        instructions.add(new Mov(Register.ACCUMULATOR, Memory.getThisPointer(sizeHelper), sizeHelper));

        instructions.add(new Call(new Immediate(CodeGenVisitor.INIT_OBJECT_FUNC), sizeHelper));

        for (final ISymbol child : constructor.children)
            instructions.addAll(platform.getBest(child));

        platform.getTileHelper().methEpilogue(instructions);
        return instructions;
    }

}
