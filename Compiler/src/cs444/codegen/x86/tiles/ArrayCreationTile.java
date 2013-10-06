package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.Runtime;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

public class ArrayCreationTile implements ITile<X86Instruction, Size, CreationExpression> {
    private static ArrayCreationTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new ArrayCreationTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).creation.add(tile);
    }

    private ArrayCreationTile() { }

    @Override
    public boolean fits(final CreationExpression creation, final Platform<X86Instruction, Size> platform) {
        return creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CreationExpression creation,
            final Platform<X86Instruction, Size> platform){

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();

        instructions.add(new Comment("Getting size for array constuction"));
        instructions.addAll(platform.getBest(creation.children.get(0)));
        instructions.add(new Comment("Save the size of the array"));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

        instructions.add(new Comment("Checking array size >= 0"));
        final String ok = "arrayCreateOk" + CodeGenVisitor.getNewLblNum();
        instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));

        instructions.add(new Jge(new Immediate(ok), sizeHelper));
        Runtime.instance.throwException(instructions, "Invalid array creation");
        instructions.add(new Label(ok));

        Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(creation.getType().value));

        if(elementSize != Size.LOW && elementSize != Size.HIGH){
            //TODO something seems to go wrong if it's shifted by 1 for some values (*2 so should not be stack alignment?)
            if(elementSize == Size.WORD) elementSize = sizeHelper.getDefaultSize();
            instructions.add(new Shl(Register.ACCUMULATOR, X86SizeHelper.getPowerSizeImd(elementSize), sizeHelper));
        }

        instructions.add(new Comment("Adding space for SIT, cast info, and length " + typeDclNode.fullName));
        //Int + object's size
        final long baseSize = platform.getObjectLayout().objSize() + sizeHelper.getIntSize(sizeHelper.getPushSize(Size.DWORD));
        final Immediate sizeI = new Immediate(baseSize);
        instructions.add(new Add(Register.ACCUMULATOR, sizeI, sizeHelper));
        instructions.add(new Comment("Allocate for array" + typeDclNode.fullName));
        platform.getRunime().mallocClear(instructions);
        instructions.add(new Comment("Pop the size"));
        instructions.add(new Pop(Register.DATA, sizeHelper));

        final long lengthIndex = platform.getObjectLayout().objSize();
        final Immediate li = new Immediate(String.valueOf(lengthIndex));

        instructions.add(new Mov(new Memory(Register.ACCUMULATOR, li), Register.DATA, sizeHelper));
        platform.getObjectLayout().initialize(typeDclNode, instructions);
        instructions.add(new Comment("Done creating object"));
        return instructions;
    }
}
