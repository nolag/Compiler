package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.B;
import cs444.codegen.arm.instructions.Cmp;
import cs444.codegen.arm.instructions.Eor;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.instructions.bases.Branch.Condition;
import cs444.codegen.arm.instructions.factories.StrMaker;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

public class ArrayCreationTile implements ITile<ArmInstruction, Size, CreationExpression> {
    private static ArrayCreationTile tile;

    public static ArrayCreationTile getTile() {
        if (tile == null) tile = new ArrayCreationTile();
        return tile;
    }

    private ArrayCreationTile() {}

    @Override
    public boolean fits(final CreationExpression creation, final Platform<ArmInstruction, Size> platform) {
        return creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final CreationExpression creation, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();

        instructions.add(platform.makeComment("Getting size for array constuction"));
        instructions.addAll(platform.getBest(creation.children.get(0)));
        instructions.add(platform.makeComment("Save the size of the array"));
        instructions.add(new Push(Register.R0));

        instructions.add(platform.makeComment("Checking array size >= 0"));
        final String ok = "arrayCreateOk" + CodeGenVisitor.getNewLblNum();
        instructions.add(new Eor(Register.R1, Register.R1, Register.R1, sizeHelper));
        instructions.add(new Cmp(Register.R0, Register.R1, sizeHelper));

        instructions.add(new B(Condition.GE, new ImmediateStr(ok)));
        platform.getRunime().throwException(instructions, "Invalid array creation");
        instructions.add(platform.makeLabel(ok));

        Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(creation.getType().value));

        if (elementSize != Size.B && elementSize != Size.SB) {
            //TODO something seems to go wrong in X86 so try this fist then remove when working
            if (elementSize == Size.H || elementSize == Size.SH) elementSize = sizeHelper.getDefaultSize();
            final byte shiftAmount = ArmSizeHelper.getPowerSizeImd(elementSize);
            instructions.add(new Mov(Register.R1, new ConstantShift(Register.R1, shiftAmount, Shift.LSL), sizeHelper));
        }

        instructions.add(platform.makeComment("Adding space for SIT, cast info, and length " + typeDclNode.fullName));
        //Int + object's size
        final int baseSize = (int) (platform.getObjectLayout().objSize() + sizeHelper.getIntSize(sizeHelper.getPushSize(Size.H)));
        final Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, baseSize, instructions, sizeHelper);
        instructions.add(new Add(Register.R0, Register.R0, op2, sizeHelper));
        instructions.add(platform.makeComment("Allocate for array" + typeDclNode.fullName));
        platform.getRunime().mallocClear(instructions);
        instructions.add(platform.makeComment("Pop the size"));
        instructions.add(new Pop(Register.R1));

        ArmTileHelper.makeInstruction(Register.R0, Register.R1, Register.R2, (int) platform.getObjectLayout().objSize(), StrMaker.instance,
                instructions, sizeHelper);

        platform.getObjectLayout().initialize(typeDclNode, instructions);
        instructions.add(platform.makeComment("Done creating object"));
        return instructions;
    }
}
