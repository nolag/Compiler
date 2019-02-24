package cs444.codegen.arm.tiles;

import cs444.codegen.ObjectLayout;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.*;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.StringLiteralSymbol;
import cs444.types.ArrayPkgClassResolver;
import cs444.types.PkgClassInfo;

public class StringTile implements ITile<ArmInstruction, Size, StringLiteralSymbol> {
    private static StringTile tile;

    private StringTile() {}

    public static StringTile getTile() {
        if (tile == null) {
            tile = new StringTile();
        }
        return tile;
    }

    @Override
    public boolean fits(StringLiteralSymbol op, Platform<ArmInstruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(StringLiteralSymbol stringSymbol,
                                                          Platform<ArmInstruction, Size> platform) {

        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        ObjectLayout<ArmInstruction, Size> objectLayout = platform.getObjectLayout();

        instructions.add(new Comment("allocate the string at the same time (why not).  String is" + stringSymbol.strValue));

        //2 per char + dword for int + obj size
        int defaultStack = sizeHelper.getDefaultStackSize();
        long objlen = platform.getObjectLayout().objSize();
        long charsLen = stringSymbol.strValue.length() * 4 + defaultStack + objlen;
        long length = charsLen + defaultStack + objlen;

        instructions.add(new Movw(Register.R0, new Immediate16((int) length), sizeHelper));
        //no need to zero out, it will be set for sure so the second last arg does not matter
        platform.getRunime().mallocNoClear(instructions);
        String charArray = ArrayPkgClassResolver.getArrayName(JoosNonTerminal.CHAR);
        platform.getObjectLayout().initialize(PkgClassInfo.instance.getSymbol(charArray), instructions);

        instructions.addAll(ArmSizeHelper.putInReg(Register.R2, stringSymbol.strValue.length(), sizeHelper));
        instructions.add(new Str(Register.R2, Register.R0, new Immediate12((short) (2 * defaultStack)), sizeHelper));

        char[] cs = stringSymbol.strValue.toCharArray();
        int charSize = sizeHelper.getBytePushSizeOfType(JoosNonTerminal.CHAR);
        int charSizeBits = charSize * 8;

        int i = 0;
        for (int numTogether = 1; numTogether > 0; numTogether /= 2) {
            for (; i + numTogether <= cs.length; i += numTogether) {
                StringBuilder sb = new StringBuilder("moving chars:");
                int togetherVal = 0;

                for (int j = 0; j < numTogether; j++) {
                    long c = cs[i + j];
                    togetherVal += c << (j * charSizeBits);
                    sb.append(" ").append(c);
                }

                instructions.add(new Comment(sb.toString()));

                instructions.addAll(ArmSizeHelper.putInReg(Register.R2, togetherVal, sizeHelper));

                Immediate12 place =
                        new Immediate12((short) (4 * i + platform.getObjectLayout().objSize() + defaultStack));
                instructions.add(new Str(sizeHelper.getSize(numTogether * charSize), Register.R2, Register.R0, place,
                        sizeHelper));
            }
        }

        instructions.add(new Comment("Array for new String"));
        instructions.add(new Mov(Register.R4, Register.R0, sizeHelper));
        instructions.add(new Comment("This pointer to new string"));
        Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, (int) charsLen, instructions, sizeHelper);
        instructions.add(new Add(Register.R0, Register.R0, op2, sizeHelper));
        platform.getObjectLayout().initialize(stringSymbol.getType().getTypeDclNode(), instructions);

        instructions.add(new Str(Register.R4, Register.R0, new Immediate12((short) objectLayout.objSize()),
                sizeHelper));

        instructions.add(new Comment("End of New String!"));
        return instructions;
    }
}
