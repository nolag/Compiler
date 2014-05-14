package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongOnlyTile;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class LongRemTile extends LongOnlyTile<RemainderExprSymbol> {
    private static LongRemTile tile;

    public static void init() {
        if(tile == null) tile = new LongRemTile();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).rems.add(tile);
    }

    private LongRemTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final RemainderExprSymbol rem, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final TileHelper<X86Instruction, Size> helper = platform.getTileHelper();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final IRuntime<X86Instruction> runtime = platform.getRunime();

        final long myNum = CodeGenVisitor.getNewLblNum();
        final String safeDiv = "safeDiv" + myNum;
        final String notSmallest = "notSmallest" + myNum;
        final String notSmallest2 = "divNotSmallest" + myNum;
        final String allDone = "doneDiv" + myNum;
        final String notNeg = "notNeg" + myNum;
        final String subing = "subLoop" + myNum;
        final String mainDone = "doneSub" + myNum;
        final String subCheck = "subCheck" + myNum;

        final Immediate allDoneImm = new Immediate(allDone);
        final Immediate mainDoneImm = new Immediate(mainDone);
        final Immediate subLoopImm = new Immediate(subing);
        final Immediate subCheckImm = new Immediate(subCheck);

        final Typeable numerator = (Typeable)rem.children.get(0);
        final Typeable denominator = (Typeable) rem.children.get(1);

        instructions.add(new Comment("Start long divide"));
        instructions.add(new Comment("Store the value of if it -ve in ebx"));
        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.addAll(platform.getBest(numerator));
        helper.pushLong(numerator, instructions, sizeHelper);

        instructions.addAll(platform.getBest(denominator));
        helper.makeLong(denominator, instructions, sizeHelper);

        instructions.add(new Cmp(Register.ACCUMULATOR, Immediate.ZERO, sizeHelper));
        instructions.add(new Jne(new Immediate(safeDiv), sizeHelper));
        instructions.add(new Cmp(Register.DATA, Immediate.ZERO, sizeHelper));
        instructions.add(new Jne(new Immediate(safeDiv), sizeHelper));
        runtime.throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(new Label(safeDiv));

        instructions.add(new Comment("Moving first long to EDI:ESI"));
        instructions.add(new Pop(Register.SOURCE, sizeHelper));
        instructions.add(new Pop(Register.DESTINATION, sizeHelper));

        instructions.add(new Comment("check if -ve remainder"));
        instructions.add(new And(Register.BASE, Immediate.BIT_32, sizeHelper));
        instructions.add(new And(Register.BASE, Register.DESTINATION, sizeHelper));

        instructions.add(new Comment("pushing base because it holds -ve and will be used for count"));
        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.add(new Comment("using ECX:EBX to hold the div result"));
        instructions.add(new Xor(Register.BASE, Register.BASE, sizeHelper));
        instructions.add(new Xor(Register.COUNTER, Register.COUNTER, sizeHelper));


        instructions.add(new Cmp(Register.DESTINATION, Immediate.BIT_32, sizeHelper));
        final Jne notSmallestJmp = new Jne(new Immediate(notSmallest), sizeHelper);
        instructions.add(notSmallestJmp);
        instructions.add(new Cmp(Register.SOURCE, Immediate.ZERO, sizeHelper));
        instructions.add(notSmallestJmp);
        instructions.add(new Sub(Register.SOURCE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Sbb(Register.DESTINATION, Register.DATA, sizeHelper));
        instructions.add(new Mov(Register.BASE, Immediate.ONE, sizeHelper));
        instructions.add(new Label(notSmallest));

        instructions.add(new Cmp(Register.DATA, Immediate.BIT_32, sizeHelper));
        instructions.add(new Jne(new Immediate(notSmallest2), sizeHelper));
        instructions.add(new Cmp(Register.ACCUMULATOR, Immediate.ZERO, sizeHelper));
        instructions.add(new Je(mainDoneImm, sizeHelper));
        instructions.add(new Label(notSmallest2));

        instructions.add(new Cmp(Register.DATA, Immediate.ZERO, sizeHelper));
        instructions.add(new Jge(new Immediate(notNeg), sizeHelper));
        X86_32TileHelper.negLong(Register.DATA, Register.ACCUMULATOR, instructions, sizeHelper);
        instructions.add(new Label(notNeg));

        instructions.add(new Cmp(Register.DESTINATION, Immediate.ZERO, sizeHelper));
        instructions.add(new Jge(new Immediate(subing), sizeHelper));
        X86_32TileHelper.negLong(Register.DESTINATION, Register.SOURCE, instructions, sizeHelper);

        instructions.add(new Label(subCheck));

        instructions.add(new Cmp(Register.DESTINATION, Register.DATA, sizeHelper));
        instructions.add(new Jl(mainDoneImm, sizeHelper));
        instructions.add(new Jg(subLoopImm, sizeHelper));
        instructions.add(new Cmp(Register.SOURCE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Jbe(mainDoneImm, sizeHelper));

        instructions.add(new Label(subing));
        instructions.add(new Sub(Register.SOURCE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Sbb(Register.DESTINATION, Register.DATA, sizeHelper));
        instructions.add(new Add(Register.COUNTER, Immediate.ONE, sizeHelper));
        instructions.add(new Adc(Register.BASE, Immediate.ZERO, sizeHelper));

        instructions.add(new Jmp(subCheckImm, sizeHelper));

        instructions.add(new Label(mainDone));
        instructions.add(new Comment("Pop to see if the number is -ve"));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Cmp(Register.BASE, Immediate.BIT_32, sizeHelper));
        instructions.add(new Jne(allDoneImm, sizeHelper));
        X86_32TileHelper.negLong(Register.DATA, Register.ACCUMULATOR, instructions, sizeHelper);

        instructions.add(new Label(allDone));
        return instructions;
    }
}