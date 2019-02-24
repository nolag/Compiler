package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.*;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public abstract class ArrayBaseTile implements ITile<X86Instruction, Size, ArrayAccessExprSymbol> {
    @Override
    public InstructionsAndTiming<X86Instruction> generate(ArrayAccessExprSymbol arrayAccess,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        ObjectLayout<X86Instruction, Size> objectLayout = platform.getObjectLayout();
        IRuntime<X86Instruction> runtime = platform.getRunime();

        instructions.add(new Comment("Accessing array"));
        instructions.addAll(platform.getBest(arrayAccess.children.get(0)));

        platform.getTileHelper().setupJmpNull(IRuntime.EXCEPTION_LBL, sizeHelper, instructions);

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(arrayAccess.children.get(1)));

        instructions.add(new Comment("Checking element >= 0"));
        long myVal = CodeGenVisitor.getNewLblNum();
        String ok = "arrayAccessP1Ok" + myVal;
        instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));
        instructions.add(new Jge(new Immediate(ok), sizeHelper));
        runtime.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        ok = "arrayAccessOk" + myVal;
        Memory len = new Memory(new AddMemoryFormat(Register.BASE,
                new Immediate(platform.getObjectLayout().objSize())));
        instructions.add(new Cmp(len, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Jg(new Immediate(ok), sizeHelper));
        runtime.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(arrayAccess.getType().value));
        if (elementSize != Size.LOW && elementSize != Size.HIGH) {
            instructions.add(new Shl(Register.ACCUMULATOR, X86SizeHelper
                    .getPowerSizeImd(elementSize), sizeHelper));
        }

        Immediate offset = new Immediate(objectLayout.objSize() + sizeHelper.getDefaultStackSize());
        instructions.add(new Add(Register.ACCUMULATOR, offset, sizeHelper));

        return instructions;
    }
}
