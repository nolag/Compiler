package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassInfo;

import java.util.Arrays;

public class StrAddTile implements ITile<X86Instruction, Size, AddExprSymbol> {
    private static StrAddTile tile;

    private StrAddTile() { }

    public static StrAddTile getTile() {
        if (tile == null) {
            tile = new StrAddTile();
        }
        return tile;
    }

    @Override
    public boolean fits(AddExprSymbol op, Platform<X86Instruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.STRING);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(AddExprSymbol op,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.STRING);
        ISymbol firstChild = op.children.get(0);
        ISymbol secondChild = op.children.get(1);

        instructions.add(new Comment("String add first arg"));
        platform.getTileHelper().strPartHelper(firstChild, resolver, instructions, platform);
        instructions.add(new Comment("Backup first string"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        instructions.add(new Comment("String add second arg"));
        platform.getTileHelper().strPartHelper(secondChild, resolver, instructions, platform);

        instructions.add(new Comment("Pushing second string as argument then first as this"));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Push(Register.BASE, sizeHelper));
        AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.STR_ADD, false,
                Arrays.asList(JoosNonTerminal.STRING), false);
        Immediate arg = new Immediate(APkgClassResolver.generateFullId(ms));
        if (ms.dclInResolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile) {
            instructions.add(new Extern(arg));
        }
        instructions.add(new Call(arg, sizeHelper));

        //The two arguments for string concat
        instructions.add(new Add(Register.STACK, new Immediate(sizeHelper.getDefaultStackSize() * 2), sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Comment("end of string add"));
        return instructions;
    }
}
