package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;

public class LongDivTile extends LongOnlyTile<X86Instruction, Size, DivideExprSymbol> {
    private static LongDivTile tile;

    private LongDivTile() {}

    public static LongDivTile getTile() {
        if (tile == null) {
            tile = new LongDivTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(DivideExprSymbol div, Platform<X86Instruction,
            Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        TileHelper<X86Instruction, Size> helper = platform.getTileHelper();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        IRuntime<X86Instruction> runtime = platform.getRunime();

        long myNum = CodeGenVisitor.getNewLblNum();
        String safeDiv = "safeDiv" + myNum;
        String notSmallest = "notSmallest" + myNum;
        String notSmallest2 = "divNotSmallest" + myNum;
        String allDone = "doneDiv" + myNum;
        String notNeg = "notNeg" + myNum;
        String subing = "subLoop" + myNum;
        String mainDone = "doneSub" + myNum;
        String subCheck = "subCheck" + myNum;

        Immediate allDoneImm = new Immediate(allDone);
        Immediate mainDoneImm = new Immediate(mainDone);
        Immediate subLoopImm = new Immediate(subing);
        Immediate subCheckImm = new Immediate(subCheck);

        Typeable numerator = (Typeable) div.children.get(0);
        Typeable denominator = (Typeable) div.children.get(1);

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

        instructions.add(new Comment("check if denom is -ve"));
        instructions.add(new Mov(Register.BASE, Register.DATA, sizeHelper));
        instructions.add(new And(Register.BASE, Immediate.BIT_32, sizeHelper));

        instructions.add(new Comment("Moving first long to EDI:ESI"));
        instructions.add(new Pop(Register.SOURCE, sizeHelper));
        instructions.add(new Pop(Register.DESTINATION, sizeHelper));

        instructions.add(new Comment("And to get the sign -ve if Base is 1"));
        instructions.add(new And(Register.BASE, Register.DESTINATION, sizeHelper));

        instructions.add(new Comment("pushing base because it holds -ve and will be used for count"));
        instructions.add(new Push(Register.BASE, sizeHelper));

        instructions.add(new Comment("using ECX:EBX to hold the div result"));
        instructions.add(new Xor(Register.BASE, Register.BASE, sizeHelper));
        instructions.add(new Xor(Register.COUNTER, Register.COUNTER, sizeHelper));

        instructions.add(new Cmp(Register.DESTINATION, Immediate.BIT_32, sizeHelper));
        Jne notSmallestJmp = new Jne(new Immediate(notSmallest), sizeHelper);
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
        instructions.add(new Mov(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
        instructions.add(new Mov(Register.DATA, Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Cmp(Register.BASE, Immediate.BIT_32, sizeHelper));
        instructions.add(new Jne(allDoneImm, sizeHelper));
        X86_32TileHelper.negLong(Register.DATA, Register.ACCUMULATOR, instructions, sizeHelper);

        instructions.add(new Label(allDone));
        return instructions;
    }
}