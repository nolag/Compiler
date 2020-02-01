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
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.BinOpExpr;
import cs444.parser.symbols.ast.expressions.DivideExprSymbol;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class LongDivTile<T extends BinOpExpr> extends LongOnlyTile<X86Instruction, Size, T> {
    private static LongDivTile<DivideExprSymbol> div = new LongDivTile<>(true, true);
    private static LongDivTile<RemainderExprSymbol> rem = new LongDivTile<>(false, false);
    public final boolean divide;
    public final boolean bothForNeg;

    private LongDivTile(boolean divide, boolean bothForNeg) {
        this.divide = divide;
        this.bothForNeg = bothForNeg;
    }

    @SuppressWarnings("unchecked")
    public static <T extends BinOpExpr> LongDivTile<T> getTile(boolean divide, boolean bothForNeg) {
        if (divide & bothForNeg) {
            return (LongDivTile<T>) div;
        }
        if (!(divide | bothForNeg)) {
            return (LongDivTile<T>) rem;
        }
        throw new IllegalArgumentException("This combination does not seem legal!");
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(
            T div, Platform<X86Instruction, Size> platform) {
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<>();
        TileHelper<X86Instruction, Size> helper = platform.getTileHelper();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        IRuntime<X86Instruction> runtime = platform.getRunime();

        instructions.add(platform.makeComment("Backing up registers that will be used for long divide"));
        instructions.add(new Push(Register.SOURCE, sizeHelper));
        instructions.add(new Push(Register.DESTINATION, sizeHelper));
        instructions.add(new Push(Register.BASE, sizeHelper));
        if (divide) {
            instructions.add(new Push(Register.FRAME, sizeHelper));
        }

        long myNum = CodeGenVisitor.getNewLblNum();
        Typeable numerator = (Typeable) div.children.get(0);
        Typeable denominator = (Typeable) div.children.get(1);

        instructions.addAll(platform.getBest(numerator));
        helper.makeLong(numerator, instructions, sizeHelper);
        helper.pushLong(numerator, instructions, sizeHelper);

        instructions.addAll(platform.getBest(denominator));
        helper.makeLong(numerator, instructions, sizeHelper);

        instructions.add(platform.makeComment("Check for division by zero"));
        String safeDiv = "safeDiv" + myNum;
        // Prefer the helper over the static method because it's more generic and can be re-used
        helper.setupJumpNeFalse(safeDiv, sizeHelper, instructions);
        X86TileHelper.setupJumpNe(Register.DATA, Immediate.ZERO, safeDiv, sizeHelper, instructions, Size.DWORD);
        runtime.throwException(instructions, JoosNonTerminal.DIV_ZERO);
        instructions.add(platform.makeLabel(safeDiv));

        instructions.add(platform.makeComment("Moving numerator to EDI:ESI"));
        instructions.add(new Pop(Register.SOURCE, sizeHelper));
        instructions.add(new Pop(Register.DESTINATION, sizeHelper));

        instructions.add(platform.makeComment("Negate result to ECX"));
        instructions.add(platform.makeComment("Check if numerator is -ve"));
        String noNegNum = "noNegNum" + myNum;
        instructions.add(new Mov(Register.COUNTER, Register.DESTINATION, sizeHelper));
        instructions.add(new And(Register.COUNTER, Immediate.BIT_32, sizeHelper));
        instructions.add(new Je(new Immediate(noNegNum), sizeHelper));
        instructions.add(new Not(Register.DESTINATION, sizeHelper));
        instructions.add(new Not(Register.SOURCE, sizeHelper));
        instructions.add(new Add(Register.SOURCE, Immediate.ONE, sizeHelper));
        instructions.add(new Adc(Register.DESTINATION, Immediate.ZERO, sizeHelper));
        instructions.add(platform.makeLabel(noNegNum));

        instructions.add(platform.makeComment("Check if denominator is -ve"));
        String noNegDen = "noNegDen" + myNum;
        instructions.add(new Mov(Register.BASE, Register.DATA, sizeHelper));
        instructions.add(new And(Register.BASE, Immediate.BIT_32, sizeHelper));
        instructions.add(new Je(new Immediate(noNegDen), sizeHelper));
        if (bothForNeg) {
            instructions.add(new Xor(Register.COUNTER, Immediate.BIT_32, sizeHelper));
        }
        instructions.add(new Not(Register.DATA, sizeHelper));
        instructions.add(new Not(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Add(Register.ACCUMULATOR, Immediate.ONE, sizeHelper));
        instructions.add(new Adc(Register.DATA, Immediate.ZERO, sizeHelper));
        instructions.add(platform.makeLabel(noNegDen));

        if (divide) {
            instructions.add(platform.makeComment("Store division result in EBP:EBX"));
            instructions.add(new Xor(Register.FRAME, Register.FRAME, sizeHelper));
            instructions.add(new Xor(Register.BASE, Register.BASE, sizeHelper));
        }

        instructions.add(platform.makeComment("Push the negation result, to reuse ECX for shift amount"));
        instructions.add(new Push(Register.COUNTER, sizeHelper));

        // TODO split the first div part and the second so there's no condition code
        // Also it can simplify the logic once there's nothing left for the higher bits

        instructions.add(platform.makeComment("Bit shift"));
        String largeNumberShift = "largeNumberShift" + myNum;
        String doneNumberShift = "doneNumberShift" + myNum;
        instructions.add(new Cmp(Register.DATA, Immediate.ZERO, sizeHelper));
        instructions.add(new Jne(new Immediate(largeNumberShift), sizeHelper));
        instructions.add(new Bsr(Register.COUNTER, Register.ACCUMULATOR, Size.DWORD, sizeHelper));
        instructions.add(new Sub(Register.COUNTER, Immediate.THIRTY_ONE, sizeHelper));
        instructions.add(new Neg(Register.COUNTER, sizeHelper));
        instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Shl(Register.DATA, Register.COUNTER, sizeHelper));
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Add(Register.COUNTER, Immediate.THIRTY_TWO, sizeHelper));
        instructions.add(new Jmp(new Immediate(doneNumberShift), sizeHelper));
        instructions.add(platform.makeLabel(largeNumberShift));
        instructions.add(new Bsr(Register.COUNTER, Register.DATA, Size.DWORD, sizeHelper));
        instructions.add(new Sub(Register.COUNTER, Immediate.THIRTY_ONE, sizeHelper));
        instructions.add(new Neg(Register.COUNTER, sizeHelper));
        instructions.add(new Shld(Register.DATA, Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
        instructions.add(new Shl(Register.ACCUMULATOR, Register.COUNTER, sizeHelper));
        instructions.add(platform.makeLabel(doneNumberShift));

        instructions.add(platform.makeComment("Start the actual division"));
        String divLoop = "divLoop" + myNum;
        String divLoopDone = "divLoopDone" + myNum;
        String divSmaller = "divSmaller" + myNum;
        String divBigger = "divBigger" + myNum;
        Immediate smallLbl = new Immediate(divSmaller);
        instructions.add(platform.makeLabel(divLoop));
        instructions.add(new Cmp(Register.DESTINATION, Register.DATA, sizeHelper));
        instructions.add(new Jb(smallLbl, sizeHelper));
        instructions.add(new Ja(new Immediate(divBigger), sizeHelper));
        instructions.add(new Cmp(Register.SOURCE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Jb(smallLbl, sizeHelper));
        instructions.add(platform.makeLabel(divBigger));
        instructions.add(new Sub(Register.SOURCE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Sbb(Register.DESTINATION, Register.DATA, sizeHelper));

        if (divide) {
            instructions.add(new Comment("We don't have more GPRs, so just push two then pop it when we're done"));
            instructions.add(new Push(Register.DESTINATION, sizeHelper));
            instructions.add(new Mov(Register.DESTINATION, Immediate.ONE, sizeHelper));
            String smallShift = "smallShift" + myNum;
            String doneShift = "doneShift" + myNum;
            instructions.add(new Cmp(Register.COUNTER, Immediate.THIRTY_TWO, sizeHelper));
            instructions.add(new Jl(new Immediate(smallShift), sizeHelper));
            instructions.add(new Sub(Register.COUNTER, Immediate.THIRTY_TWO, sizeHelper));
            instructions.add(new Shl(Register.DESTINATION, Register.COUNTER, sizeHelper));
            instructions.add(new Or(Register.FRAME, Register.DESTINATION, sizeHelper));
            instructions.add(new Add(Register.COUNTER, Immediate.THIRTY_TWO, sizeHelper));
            instructions.add(new Jmp(new Immediate(doneShift), sizeHelper));
            instructions.add(platform.makeLabel(smallShift));
            instructions.add(new Shl(Register.DESTINATION, Register.COUNTER, sizeHelper));
            instructions.add(new Or(Register.BASE, Register.DESTINATION, sizeHelper));
            instructions.add(platform.makeLabel(doneShift));
            instructions.add(new Pop(Register.DESTINATION, sizeHelper));
        }

        instructions.add(platform.makeLabel(divSmaller));
        instructions.add(new Cmp(Register.COUNTER, Immediate.ZERO, sizeHelper));
        instructions.add(new Je(new Immediate(divLoopDone), sizeHelper));
        instructions.add(new Sub(Register.COUNTER, Immediate.ONE, sizeHelper));
        instructions.add(new Shrd(Register.ACCUMULATOR, Register.DATA, Immediate.ONE, sizeHelper));
        instructions.add(new Shr(Register.DATA, Immediate.ONE, sizeHelper));
        instructions.add(new Jmp(new Immediate(divLoop), sizeHelper));
        instructions.add(platform.makeLabel(divLoopDone));

        if (divide) {
            instructions.add(new Mov(Register.DATA, Register.FRAME, sizeHelper));
            instructions.add(new Mov(Register.ACCUMULATOR, Register.BASE, sizeHelper));
        } else {
            instructions.add(new Mov(Register.DATA, Register.DESTINATION, sizeHelper));
            instructions.add(new Mov(Register.ACCUMULATOR, Register.SOURCE, sizeHelper));
        }

        String noNeg = "notNegative" + myNum;
        instructions.add(platform.makeComment("Fix negative numbers"));
        instructions.add(new Pop(Register.COUNTER, sizeHelper));
        // TODO instead of shifting one in the counter for divide, just the value 1 << to the counter >> instead of substracting
        instructions.add(new Cmp(Register.COUNTER, Immediate.ZERO, sizeHelper));
        instructions.add(new Je(new Immediate(noNeg), sizeHelper));
        instructions.add(new Not(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Not(Register.DATA, sizeHelper));
        instructions.add(new Add(Register.ACCUMULATOR, Immediate.ONE, sizeHelper));
        instructions.add(new Adc(Register.DATA, Immediate.ZERO, sizeHelper));
        instructions.add(platform.makeLabel(noNeg));

        instructions.add(platform.makeComment("Pop saved registers"));
        if (divide) {
            instructions.add(new Pop(Register.FRAME, sizeHelper));
        }

        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.DESTINATION, sizeHelper));
        instructions.add(new Pop(Register.SOURCE, sizeHelper));
        return instructions;
    }
}