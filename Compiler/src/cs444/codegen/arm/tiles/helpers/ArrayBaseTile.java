package cs444.codegen.arm.tiles.helpers;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.B;
import cs444.codegen.arm.instructions.Cmp;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Label;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public abstract class ArrayBaseTile implements ITile<ArmInstruction, Size, ArrayAccessExprSymbol> {
    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final ObjectLayout<ArmInstruction, Size> objectLayout = platform.getObjectLayout();
        final IRuntime<ArmInstruction> runtime = platform.getRunime();

        instructions.add(new Comment("Accessing array"));
        instructions.addAll(platform.getBest(arrayAccess.children.get(0)));

        platform.getTileHelper().setupJmpNull(IRuntime.EXCEPTION_LBL, sizeHelper, instructions);

        instructions.add(new Push(Register.R8));
        instructions.add(new Mov(Register.R8, Register.R0, sizeHelper));
        instructions.addAll(platform.getBest(arrayAccess.children.get(1)));

        instructions.add(new Comment("Checking element >= 0"));
        final long myVal = CodeGenVisitor.getNewLblNum();
        String ok = "arrayAccessP1Ok" + myVal;
        instructions.add(new Eor(Register.R1, Register.R1, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R0, Register.R1, sizeHelper));
        instructions.add(new B(Condition.GE, ok));
        runtime.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        ok = "arrayAccessOk" + myVal;
        instructions.add(new Ldr(Register.R8, Register.R8, new Immediate12((short) platform.getObjectLayout().objSize()), sizeHelper));
        instructions.add(new Cmp(Register.R8, Register.R0, sizeHelper));

        instructions.add(new B(Condition.GT, ok));
        runtime.throwException(instructions, "Invalid array access");
        instructions.add(new Label(ok));

        final Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(arrayAccess.getType().value));
        if (elementSize != Size.B && elementSize != Size.SB) {
            instructions.add(new Mov(Register.R0, new ConstantShift(Register.R0, ArmSizeHelper.getPowerSizeImd(elementSize), Shift.LSL),
                    sizeHelper));
        }

        final Immediate8 offset = new Immediate8((char) (objectLayout.objSize() + sizeHelper.getDefaultStackSize()));
        instructions.add(new Add(Register.R0, Register.R0, offset, sizeHelper));

        return instructions;
    }
}
