package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Jmp;
import cs444.codegen.x86.instructions.Label;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.InstanceOfExprSymbol;

public class InstanceOfTile implements ITile<X86Instruction, Size, InstanceOfExprSymbol>{
    public static void init(){
        new InstanceOfTile();
    }

    private InstanceOfTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).insts.add(this);
    }

    @Override
    public boolean fits(final InstanceOfExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final InstanceOfExprSymbol op, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.addAll(platform.getBest(op.getLeftOperand()));
        // eax should have reference to object
        final String nullObjectLbl = "nullObject" + CodeGenVisitor.getNewLblNum();
        platform.getTileHelper().ifNullJmpCode(nullObjectLbl, sizeHelper, instructions);

        instructions.addAll(platform.getObjectLayout().subtypeCheckCode((TypeSymbol) op.getRightOperand(), platform));

        final String endLbl = "instanceOfEnd" + CodeGenVisitor.getNewLblNum();
        instructions.add(new Jmp(new Immediate(endLbl), sizeHelper));

        instructions.add(new Label(nullObjectLbl));
        instructions.add(new Comment("set eax to FALSE"));
        instructions.add(new Mov(Register.ACCUMULATOR, Immediate.FALSE, sizeHelper));

        instructions.add(new Label(endLbl));
        return instructions;
    }
}
