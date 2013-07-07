package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Add;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;

public class ANonTerminalTile implements ITile<X86Instruction, X86SizeHelper, ANonTerminal>{
    public static void init(){
        new ANonTerminalTile();
    }

    private ANonTerminalTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).anonTerms.add(this);
    }

    @Override
    public boolean fits(final ANonTerminal symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ANonTerminal aNonTerminal,
            final Platform<X86Instruction, X86SizeHelper> platform){

        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final boolean isBlock = aNonTerminal.getName().equals(JoosNonTerminal.BLOCK);
        final CodeGenVisitor codeGen = CodeGenVisitor.getCurrentCodeGen();

        for(final ISymbol child : aNonTerminal.children) instructions.addAll(platform.getBest(child));

        if(isBlock && !codeGen.lastWasFunc){
            final long size = aNonTerminal.getStackSize();
            if(0 != size){
                final Immediate by = new Immediate(String.valueOf(size));
                instructions.add(new Add(Register.STACK, by, sizeHelper));
            }
        }
        return instructions;
    }
}
