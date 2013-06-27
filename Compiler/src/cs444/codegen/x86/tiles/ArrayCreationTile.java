package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Add;
import cs444.codegen.instructions.x86.Cmp;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Jge;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.Shl;
import cs444.codegen.instructions.x86.Xor;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

public class ArrayCreationTile implements ITile<X86Instruction, CreationExpression>{
    public static void init(){
        new ArrayCreationTile();
    }

    private ArrayCreationTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).creation.add(this);
    }

    @Override
    public boolean fits(final CreationExpression creation) {
        return creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CreationExpression creation, final Platform<X86Instruction> platform){
        final CodeGenVisitor visitor = CodeGenVisitor.getCurrentCodeGen();

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();
        final APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();

        instructions.add(new Comment("Getting size for array constuction"));
        instructions.addAll(platform.getBest(creation.children.get(0)));
        instructions.add(new Comment("Save the size of the array"));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

        instructions.add(new Comment("Checking array size >= 0"));
        final String ok = "arrayCreateOk" + visitor.getNewLblNum();
        instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));

        instructions.add(new Jge(new Immediate(ok), sizeHelper));
        Runtime.instance.throwException(instructions, "Invalid array creation");
        instructions.add(new Label(ok));

        final Size lastSize = visitor.lastSize;

        if(lastSize != Size.LOW && lastSize != Size.HIGH)
            instructions.add(new Shl(Register.ACCUMULATOR, X86SizeHelper.getPowerSizeImd(lastSize), sizeHelper));

        instructions.add(new Comment("Adding space for SIT, cast info, and length" + typeDclNode.fullName));
        //Int + object's sie
        final long baseSize = platform.getObjectLayout().objSize() + X86SizeHelper.getIntSize(Size.DWORD);
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
