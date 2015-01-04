package cs444.codegen.arm.tiles;

import java.util.Arrays;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.AMethodSymbol;
import cs444.parser.symbols.ast.expressions.AddExprSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassInfo;

public class StrAddTile implements ITile<ArmInstruction, Size, AddExprSymbol> {
    private static StrAddTile tile;

    public static StrAddTile getTile() {
        if (tile == null) tile = new StrAddTile();
        return tile;
    }

    private StrAddTile() {}

    @Override
    public boolean fits(final AddExprSymbol op, final Platform<ArmInstruction, Size> platform) {
        return op.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.STRING);
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final AddExprSymbol op, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();
        final APkgClassResolver resolver = PkgClassInfo.instance.getSymbol(JoosNonTerminal.STRING);
        final ISymbol firstChild = op.children.get(0);
        final ISymbol secondChild = op.children.get(1);

        instructions.add(new Comment("String add first arg"));
        tileHelper.strPartHelper(firstChild, resolver, instructions, platform);
        instructions.add(new Comment("Backup first string"));
        instructions.add(new Push(Register.R8));
        instructions.add(new Mov(Register.R8, Register.R0, sizeHelper));

        instructions.add(new Comment("String add second arg"));
        tileHelper.strPartHelper(secondChild, resolver, instructions, platform);

        instructions.add(new Comment("Pushing second string as arument then first as this"));
        instructions.add(new Push(Register.R0));
        instructions.add(new Push(Register.R8));

        final AMethodSymbol ms = resolver.safeFindMethod(JoosNonTerminal.STR_ADD, false, Arrays.asList(JoosNonTerminal.STRING), false);
        if (ms.dclInResolver != CodeGenVisitor.<ArmInstruction, Size> getCurrentCodeGen(platform).currentFile) {
            instructions.add(platform.makeExtern(APkgClassResolver.generateFullId(ms)));
        }

        tileHelper.makeCall(APkgClassResolver.generateFullId(ms), instructions, sizeHelper);

        //The two arguments for string concat
        final Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, sizeHelper.getDefaultStackSize() * 2, instructions, sizeHelper);
        instructions.add(new Add(Register.STACK, Register.STACK, op2, sizeHelper));
        instructions.add(new Pop(Register.R8));
        instructions.add(new Comment("end of string add"));
        return instructions;
    }
}
