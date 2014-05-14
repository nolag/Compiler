package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;

public class ANonTerminalTile implements ITile<X86Instruction, Size, ANonTerminal> {
    private static ANonTerminalTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new ANonTerminalTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).anonTerms.add(tile);
    }

    private ANonTerminalTile() { }

    @Override
    public boolean fits(final ANonTerminal symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ANonTerminal aNonTerminal, final Platform<X86Instruction, Size> platform){

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final boolean isBlock = aNonTerminal.getName().equals(JoosNonTerminal.BLOCK);
        final CodeGenVisitor<X86Instruction, Size> codeGen = CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform);

        for(final ISymbol child : aNonTerminal.children) instructions.addAll(platform.getBest(child));

        if(isBlock && !codeGen.lastWasFunc){
            final long size = aNonTerminal.getStackSize(platform);
            if(0 != size){
                final Immediate by = new Immediate(String.valueOf(size));
                instructions.add(new Add(Register.STACK, by, sizeHelper));
            }
        }
        return instructions;
    }
}
