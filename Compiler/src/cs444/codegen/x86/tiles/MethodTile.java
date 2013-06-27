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

public class MethodTile implements ITile<X86Instruction, MethodSymbol>{
    public static void init(){
        new MethodTile();
    }

    private MethodTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).methods.add(this);
    }

    @Override
    public boolean fits(final MethodSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final MethodSymbol method, final Platform<X86Instruction> platform) {
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();
        final String methodName = APkgClassResolver.generateFullId(method);

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        if(method.isNative()){
            instructions.add(new Extern(methodName));
        }else{
            instructions.addAll(TileHelper.methProlog(method, methodName, sizeHelper));
            for(final ISymbol child : method.children) instructions.addAll(platform.getBest(child));
            instructions.addAll(TileHelper.methEpilogue(method));
        }
        return instructions;
    }
}
