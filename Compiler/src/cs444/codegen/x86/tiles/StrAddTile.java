package cs444.codegen.x86.tiles;

import java.util.Arrays;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.*;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassInfo;

public class StrAddTile implements ITile<X86Instruction, X86SizeHelper, AddExprSymbol>{
    public static void init(){
        new StrAddTile();
    }

    private StrAddTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).adds.add(this);
    }

    @Override
    public boolean fits(final AddExprSymbol op) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.STRING);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final AddExprSymbol op,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.STRING);
        final ISymbol firstChild = op.children.get(0);
        final ISymbol secondChild = op.children.get(1);

        instructions.add(new Comment("String add first arg"));
        TileHelper.strPartHelper(firstChild, resolver, instructions, platform);
        instructions.add(new Comment("Backup first string"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        instructions.add(new Comment("String add second arg"));
        TileHelper.strPartHelper(secondChild, resolver, instructions, platform);

        instructions.add(new Comment("Pushing second string as arument then first as this"));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Push(Register.BASE, sizeHelper));
        final AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.STR_ADD, false, Arrays.asList(JoosNonTerminal.STRING), false);
        final Immediate arg = new Immediate(APkgClassResolver.generateFullId(ms));
        if(ms.dclInResolver != CodeGenVisitor.getCurrentCodeGen().currentFile) instructions.add(new Extern(arg));
        instructions.add(new Call(arg, sizeHelper));

        //The two arguments for string concat
        instructions.add(new Add(Register.STACK, new Immediate(sizeHelper.defaultStackSize * 2), sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(new Comment("end of string add"));
        return instructions;
    }
}