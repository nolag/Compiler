package cs444.codegen.x86.tiles;

import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.types.ArrayPkgClassResolver;
import cs444.types.PkgClassInfo;

public class StringTile implements ITile<X86Instruction, Size, StringLiteralSymbol> {
    private static StringTile tile;

    private StringTile() {}

    public static StringTile getTile() {
        if (tile == null) {
            tile = new StringTile();
        }
        return tile;
    }

    @Override
    public boolean fits(StringLiteralSymbol op, Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(StringLiteralSymbol stringSymbol,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        ObjectLayout<X86Instruction, Size> objectLayout = platform.getObjectLayout();

        instructions.add(new Comment("allocate the string at the same time (why not)"));

        //2 per char + dword for int + obj size
        int defaultStack = sizeHelper.getDefaultStackSize();
        long objlen = platform.getObjectLayout().objSize();
        long charsLen = stringSymbol.strValue.length() * 2 + defaultStack + objlen;
        long length = charsLen + defaultStack + objlen;

        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(length), sizeHelper));
        //no need to zero out, it will be set for sure so the second last arg does not matter
        platform.getRunime().mallocNoClear(instructions);
        String charArray = ArrayPkgClassResolver.getArrayName(JoosNonTerminal.CHAR);
        platform.getObjectLayout().initialize(PkgClassInfo.instance.getSymbol(charArray), instructions);

        Memory where = new Memory(new AddMemoryFormat(Register.ACCUMULATOR, new Immediate(2 * defaultStack)));
        instructions.add(new Mov(where, new Immediate(stringSymbol.strValue.length()), sizeHelper));

        char[] cs = stringSymbol.strValue.toCharArray();
        int charSize = sizeHelper.getBytePushSizeOfType(JoosNonTerminal.CHAR);
        int charSizeBits = charSize * 8;

        int i = 0;

        //can't move 64 bit immediate value, so numTogether can't be more than 2.
        for (int numTogether = 2; numTogether > 0; numTogether /= 2) {
            for (; i + numTogether <= cs.length; i += numTogether) {
                StringBuilder sb = new StringBuilder("moving chars:");
                long togetherVal = 0;

                for (int j = 0; j < numTogether; j++) {
                    long c = cs[i + j];
                    togetherVal += c << (j * charSizeBits);
                    sb.append(" ").append(c);
                }

                Immediate place = new Immediate(2 * i + platform.getObjectLayout().objSize() + defaultStack);
                Memory to = new Memory(new AddMemoryFormat(Register.ACCUMULATOR, place));
                instructions.add(new Comment(sb.toString()));
                instructions.add(new Mov(to, new Immediate(togetherVal), sizeHelper.getSize(numTogether * charSize),
                        sizeHelper));
            }
        }

        instructions.add(new Comment("Array for new String"));
        instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Comment("This pointer to new string"));
        instructions.add(new Add(Register.ACCUMULATOR, new Immediate(charsLen), sizeHelper));
        platform.getObjectLayout().initialize(stringSymbol.getType().getTypeDclNode(), instructions);
        Memory memory = new Memory(new AddMemoryFormat(Register.ACCUMULATOR,
                new Immediate(objectLayout.objSize())));
        instructions.add(new Mov(memory, Register.DATA, sizeHelper));

        instructions.add(new Comment("End of New String!"));
        return instructions;
    }
}
