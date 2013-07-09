package cs444.codegen.x86.tiles;

import java.util.Arrays;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.*;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.ArrayPkgClassResolver;
import cs444.types.PkgClassInfo;
import cs444.types.exceptions.UndeclaredException;

public class StringTile implements ITile<X86Instruction, X86SizeHelper, StringLiteralSymbol>{
    public static void init(){
        new StringTile();
    }

    private StringTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).strs.add(this);
    }

    @Override
    public boolean fits(final StringLiteralSymbol op) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final StringLiteralSymbol stringSymbol,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        instructions.add(new Comment("allocate the string at the same time (why not)"));

        final APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.STRING);

        //2 per char + dword for int + obj size
        final long objlen = platform.getObjectLayout().objSize();
        final long charsLen = stringSymbol.strValue.length() * 2 + sizeHelper.getIntSize(Size.DWORD) + objlen;
        final long length =  charsLen + resolver.getStackSize(sizeHelper) + objlen;

        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(length), sizeHelper));
        //no need to zero out, it will be set for sure so the second last arg does not matter
        Runtime.instance.mallocNoClear(instructions);
        final String charArray = ArrayPkgClassResolver.getArrayName(JoosNonTerminal.CHAR);
        platform.getObjectLayout().initialize(PkgClassInfo.instance.getSymbol(charArray), instructions);

        instructions.add(new Mov(new Memory(Register.ACCUMULATOR, 8), new Immediate(stringSymbol.strValue.length()), sizeHelper));

        final char [] cs = stringSymbol.strValue.toCharArray();
        for(int i = 0; i < cs.length; i++){
            final long place = 2 * i + platform.getObjectLayout().objSize() + sizeHelper.getIntSize(Size.DWORD);
            final Memory to = new Memory(Register.ACCUMULATOR, new Immediate(String.valueOf(place)));
            instructions.add(new Mov(to, new Immediate((cs[i])), Size.WORD, sizeHelper));
        }
        try {
            final String arg = charArray;
            final ConstructorSymbol constructor = resolver.getConstructor(Arrays.asList(arg), resolver);
            instructions.add(new Comment("First arg to new String"));
            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Comment("This pointer to new string"));
            instructions.add(new Add(Register.ACCUMULATOR, new Immediate(charsLen), sizeHelper));
            platform.getObjectLayout().initialize(resolver, instructions);
            instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));

            final Immediate carg = new Immediate(APkgClassResolver.generateFullId(constructor));
            if(constructor.dclInResolver != CodeGenVisitor.getCurrentCodeGen().currentFile) instructions.add(new Extern(carg));

            instructions.add(new Call(carg, sizeHelper));
            instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
            instructions.add(new Add(Register.STACK, new Immediate(sizeHelper.defaultStackSize), sizeHelper));

        } catch (final UndeclaredException e) {
            //Should never get here
            e.printStackTrace();
        }

        instructions.add(new Comment("End of New String!"));
        return instructions;
    }

}
