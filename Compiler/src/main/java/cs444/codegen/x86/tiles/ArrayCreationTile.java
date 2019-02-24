package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

public class ArrayCreationTile implements ITile<X86Instruction, Size, CreationExpression> {
    private static ArrayCreationTile tile;

    private ArrayCreationTile() {}

    public static ArrayCreationTile getTile() {
        if (tile == null) {
            tile = new ArrayCreationTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CreationExpression creation, Platform<X86Instruction, Size> platform) {
        return creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(CreationExpression creation,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();

        instructions.add(platform.makeComment("Getting size for array constuction"));
        instructions.addAll(platform.getBest(creation.children.get(0)));
        instructions.add(platform.makeComment("Save the size of the array"));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

        instructions.add(platform.makeComment("Checking array size >= 0"));
        String ok = "arrayCreateOk" + CodeGenVisitor.getNewLblNum();
        instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));

        instructions.add(new Jge(new Immediate(ok), sizeHelper));
        platform.getRunime().throwException(instructions, "Invalid array creation");
        instructions.add(platform.makeLabel(ok));

        Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(creation.getType().value));

        if (elementSize != Size.LOW && elementSize != Size.HIGH) {
            //TODO something seems to go wrong if it's shifted by 1 for some values (*2 so should not be stack
            // alignment?)
            if (elementSize == Size.WORD) {
                elementSize = sizeHelper.getDefaultSize();
            }
            instructions.add(new Shl(Register.ACCUMULATOR, X86SizeHelper.getPowerSizeImd(elementSize), sizeHelper));
        }

        instructions.add(platform.makeComment("Adding space for SIT, cast info, and length " + typeDclNode.fullName));
        //Int + object's size
        long baseSize =
                platform.getObjectLayout().objSize() + sizeHelper.getIntSize(sizeHelper.getPushSize(Size.DWORD));
        Immediate sizeI = new Immediate(baseSize);
        instructions.add(new Add(Register.ACCUMULATOR, sizeI, sizeHelper));
        instructions.add(platform.makeComment("Allocate for array" + typeDclNode.fullName));
        platform.getRunime().mallocClear(instructions);
        instructions.add(platform.makeComment("Pop the size"));
        instructions.add(new Pop(Register.DATA, sizeHelper));

        Immediate lengthIndex = new Immediate(platform.getObjectLayout().objSize());

        instructions.add(new Mov(new Memory(new AddMemoryFormat(Register.ACCUMULATOR, lengthIndex)), Register.DATA,
                sizeHelper));
        platform.getObjectLayout().initialize(typeDclNode, instructions);
        instructions.add(platform.makeComment("Done creating object"));
        return instructions;
    }
}
