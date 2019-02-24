package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.*;
import cs444.codegen.arm.*;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public abstract class ArrayBaseTile implements ITile<ArmInstruction, Size, ArrayAccessExprSymbol> {
    @Override
    public InstructionsAndTiming<ArmInstruction> generate(ArrayAccessExprSymbol arrayAccess,
                                                          Platform<ArmInstruction, Size> platform) {

        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        ObjectLayout<ArmInstruction, Size> objectLayout = platform.getObjectLayout();
        IRuntime<ArmInstruction> runtime = platform.getRunime();

        instructions.add(new Comment("Accessing array"));
        instructions.addAll(platform.getBest(arrayAccess.children.get(0)));

        platform.getTileHelper().setupJmpNull(IRuntime.EXCEPTION_LBL, sizeHelper, instructions);

        instructions.add(new Push(Register.R8));
        instructions.add(new Mov(Register.R8, Register.R0, sizeHelper));
        instructions.addAll(platform.getBest(arrayAccess.children.get(1)));

        instructions.add(new Comment("Checking element >= 0"));
        long myVal = CodeGenVisitor.getNewLblNum();
        String ok = "arrayAccessP1Ok" + myVal;
        instructions.add(new Eor(Register.R1, Register.R1, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R0, Register.R1, sizeHelper));
        instructions.add(new B(Condition.GE, ok));
        runtime.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        ok = "arrayAccessOk" + myVal;
        instructions.add(new Ldr(Register.R3, Register.R8,
                new Immediate12((short) platform.getObjectLayout().objSize()), sizeHelper));
        instructions.add(new Cmp(Register.R3, Register.R0, sizeHelper));

        instructions.add(new B(Condition.GT, ok));
        runtime.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(arrayAccess.getType().value));
        if (elementSize != Size.B && elementSize != Size.SB) {
            instructions.add(new Mov(Register.R0, new ConstantShift(Register.R0,
                    ArmSizeHelper.getPowerSizeImd(elementSize), Shift.LSL),
                    sizeHelper));
        }

        Immediate8 offset = new Immediate8((char) (objectLayout.objSize() + sizeHelper.getDefaultStackSize()));
        instructions.add(new Add(Register.R0, Register.R0, offset, sizeHelper));

        return instructions;
    }
}
