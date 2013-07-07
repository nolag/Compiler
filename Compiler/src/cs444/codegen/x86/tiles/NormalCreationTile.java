package cs444.codegen.x86.tiles;

import java.util.List;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

public class NormalCreationTile implements ITile<X86Instruction, X86SizeHelper, CreationExpression>{
    public static void init(){
        new NormalCreationTile();
    }

    private NormalCreationTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).creation.add(this);
    }

    @Override
    public boolean fits(final CreationExpression creation) {
        return !creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CreationExpression creation,
            final Platform<X86Instruction, X86SizeHelper> platform){

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final long allocSize = typeDclNode.getStackSize(sizeHelper) + platform.getObjectLayout().objSize();
        final InstructionArg bytes = new Immediate(allocSize);
        instructions.add(new Comment("Allocate " + allocSize + " bytes for " + typeDclNode.fullName));
        instructions.add(new Mov(Register.ACCUMULATOR, bytes, sizeHelper));

        platform.getRunime().mallocClear(instructions);

        platform.getObjectLayout().initialize(typeDclNode, instructions);

        final APkgClassResolver resolver = creation.getType().getTypeDclNode();

        final List<ISymbol> children = creation.children;

        instructions.add(new Comment("invoke Constructor"));

        TileHelper.invokeConstructor(resolver, children, platform, instructions);
        instructions.add(new Comment("Done creating object"));
        return instructions;
    }
}
