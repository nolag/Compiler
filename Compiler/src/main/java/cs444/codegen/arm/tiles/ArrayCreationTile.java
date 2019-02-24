package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.instructions.*;
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

    private ArrayCreationTile() {}

    public static ArrayCreationTile getTile() {
        if (tile == null) {
            tile = new ArrayCreationTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CreationExpression creation, Platform<ArmInstruction, Size> platform) {
        return creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(CreationExpression creation,
                                                          Platform<ArmInstruction, Size> platform) {

        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();

        instructions.add(platform.makeComment("Getting size for array constuction"));
        instructions.addAll(platform.getBest(creation.children.get(0)));
        instructions.add(platform.makeComment("Save the size of the array"));
        instructions.add(new Push(Register.R0));

        instructions.add(platform.makeComment("Checking array size >= 0"));
        String ok = "arrayCreateOk" + CodeGenVisitor.getNewLblNum();
        instructions.add(new Eor(Register.R2, Register.R2, Register.R2, sizeHelper));
        instructions.add(new Cmp(Register.R0, Register.R2, sizeHelper));

        instructions.add(new B(Condition.GE, ok));
        platform.getRunime().throwException(instructions, "Invalid array creation");
        instructions.add(platform.makeLabel(ok));

        Size elementSize = sizeHelper.getPushSize(sizeHelper.getSizeOfType(creation.getType().value));

        if (elementSize != Size.B && elementSize != Size.SB) {
            //TODO something seems to go wrong in X86 so try this fist then remove when working
            if (elementSize == Size.H || elementSize == Size.SH) {
                elementSize = sizeHelper.getDefaultSize();
            }
            byte shiftAmount = ArmSizeHelper.getPowerSizeImd(elementSize);
            instructions.add(new Mov(Register.R0, new ConstantShift(Register.R0, shiftAmount, Shift.LSL), sizeHelper));
        }

        instructions.add(platform.makeComment("Adding space for SIT, cast info, and length " + typeDclNode.fullName));
        //Int + object's size
        int baseSize =
                (int) (platform.getObjectLayout().objSize() + sizeHelper.getIntSize(sizeHelper.getPushSize(Size.W)));
        Operand2 op2 = ArmTileHelper.setupOp2(Register.R2, baseSize, instructions, sizeHelper);
        instructions.add(new Add(Register.R0, Register.R0, op2, sizeHelper));
        instructions.add(platform.makeComment("Allocate for array" + typeDclNode.fullName));
        platform.getRunime().mallocClear(instructions);
        instructions.add(platform.makeComment("Pop the size"));
        instructions.add(new Pop(Register.R2));

        ArmTileHelper.makeInstruction(Register.R2, Register.R0, Register.R1,
                (int) platform.getObjectLayout().objSize(), StrMaker.instance,
                instructions, sizeHelper);

        platform.getObjectLayout().initialize(typeDclNode, instructions);
        instructions.add(platform.makeComment("Done creating object"));
        return instructions;
    }
}
