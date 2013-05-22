package cs444.codegen.x86.tiles;

import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Movsx;
import cs444.codegen.instructions.x86.Movzx;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;

public class TileHelper {
    public static void genMov(final Size size, final InstructionArg from, final String value, final Typeable dcl,
            final InstructionsAndTiming<X86Instruction> instructions, final X86SizeHelper sizeHelper){

        X86Instruction instruction;

        if(size == Size.DWORD){
            instruction = new Mov(Register.ACCUMULATOR, from, sizeHelper);
        }else if(JoosNonTerminal.unsigned.contains(dcl.getType().getTypeDclNode().fullName)){
            instruction = new Movzx(Register.ACCUMULATOR, from, size, sizeHelper);
        }else{
            instruction = new Movsx(Register.ACCUMULATOR, from, size, sizeHelper);
        }

        instructions.add(new Comment("getting value of " + value));
        instructions.add(instruction);
    }
}
