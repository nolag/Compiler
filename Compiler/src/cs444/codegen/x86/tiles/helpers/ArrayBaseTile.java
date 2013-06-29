package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.*;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public abstract class ArrayBaseTile implements ITile<X86Instruction, ArrayAccessExprSymbol>{
    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ArrayAccessExprSymbol arrayAccess, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper)platform.getSizeHelper();
        final CodeGenVisitor visitor = CodeGenVisitor.getCurrentCodeGen();

        instructions.add(new Comment("Accessing array"));
        instructions.addAll(platform.getBest(arrayAccess.children.get(0)));

        TileHelper.ifNullJmpCode(Register.ACCUMULATOR, Runtime.EXCEPTION_LBL, sizeHelper, instructions);

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(arrayAccess.children.get(1)));

        instructions.add(new Comment("Checking element >= 0"));
        String ok = "arrayAccessOk" + visitor.getNewLblNum();
        instructions.add(new Xor(Register.DATA, Register.DATA, sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Register.DATA, sizeHelper));
        instructions.add(new Jge(new Immediate(ok), sizeHelper));
        Runtime.instance.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        ok = "arrayAccessOk" + visitor.getNewLblNum();
        final Memory len = new Memory(Register.BASE, new Immediate(platform.getObjectLayout().objSize()));
        instructions.add(new Cmp(len, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Jg(new Immediate(ok), sizeHelper));
        Runtime.instance.throwException(instructions, "Invalid array creation");
        instructions.add(new Label(ok));

        final long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
        Size elementSize;
        if(stackSize >= sizeHelper.defaultStackSize) elementSize = sizeHelper.defaultStack;
        else elementSize = X86SizeHelper.getSize(stackSize);

        instructions.add(new Shl(Register.ACCUMULATOR, Immediate.getImediateShift(X86SizeHelper.getPushSize(elementSize)), sizeHelper));
        final long offset = sizeHelper.defaultStackSize * 2 + X86SizeHelper.getIntSize(Size.DWORD);
        instructions.add(new Add(Register.ACCUMULATOR, new Immediate(offset), sizeHelper));
        return instructions;
    }

}
