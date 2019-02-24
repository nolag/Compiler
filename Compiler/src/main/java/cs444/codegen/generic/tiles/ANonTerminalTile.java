package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;

@SuppressWarnings("rawtypes")
public class ANonTerminalTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, ANonTerminal> {

    private static ANonTerminalTile tile;

    private ANonTerminalTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ANonTerminalTile<T, E> getTile() {
        if (tile == null) {
            tile = new ANonTerminalTile();
        }
        return tile;
    }

    @Override
    public boolean fits(ANonTerminal symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(ANonTerminal aNonTerminal, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        boolean isBlock = aNonTerminal.getName().equals(JoosNonTerminal.BLOCK);
        CodeGenVisitor<T, E> codeGen = CodeGenVisitor.getCurrentCodeGen(platform);

        for (ISymbol child : aNonTerminal.children) {
            instructions.addAll(platform.getBest(child));
        }

        if (isBlock && !codeGen.lastWasFunc) {
            long size = aNonTerminal.getStackSize(platform);
            platform.getTileHelper().cleanStackSpace(aNonTerminal.name, instructions, size, platform);
        }
        return instructions;
    }
}
