package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Extern;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.types.APkgClassResolver;

public class MethodTile implements ITile<X86Instruction, X86SizeHelper, MethodSymbol>{
    public static void init(){
        new MethodTile();
    }

    private MethodTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).methods.add(this);
    }

    @Override
    public boolean fits(final MethodSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final MethodSymbol method,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final String methodName = APkgClassResolver.generateFullId(method);

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        if(method.isNative()){
            instructions.add(new Extern(methodName));
        }else{
            TileHelper.methProlog(method, methodName, sizeHelper, instructions);
            for(final ISymbol child : method.children) instructions.addAll(platform.getBest(child));
            TileHelper.methEpilogue(method, instructions);
        }
        return instructions;
    }
}
