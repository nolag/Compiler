package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericHelperTile;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;

public class AssignmentTile extends NumericHelperTile<AssignmentExprSymbol> {
    private static AssignmentTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new AssignmentTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).assigns.add(tile);
    }

    private AssignmentTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final AssignmentExprSymbol op, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();


        final Typeable leftHandSide = (Typeable) op.children.get(0);
        final Typeable rightHandSide = (Typeable) op.children.get(1);

        instructions.add(new Comment("Start Assignment " + leftHandSide.getName() + "=" + rightHandSide.getName()));
        instructions.addAll(platform.getBest(leftHandSide));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(rightHandSide));

        instructions.add(new Pop(Register.DATA, sizeHelper));
        final Memory to = new Memory(Register.DATA);

        final Size size = CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).lhsSize;

        if(leftHandSide.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)) {
            tileHelper.makeLong(rightHandSide, instructions, sizeHelper);
        }

        instructions.add(new Mov(to, Register.ACCUMULATOR, size, sizeHelper));
        instructions.add(new Comment("End Assignment"));
        return instructions;
    }

}
