package cs444.codegen.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class ZeroMultTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, MultiplyExprSymbol>{

    public static <T extends Instruction, E extends Enum<E>> void init(final Class<T> klass){
        new ZeroMultTile<T, E>(klass);
    }

    private ZeroMultTile(final Class<T> klass){
        TileSet.<T, E>getOrMake(klass).mults.add(this);
    }

    @Override
    public boolean fits(final MultiplyExprSymbol symbol, final Platform<T, E> platform) {
        final ISymbol first = symbol.children.get(0);
        final ISymbol second = symbol.children.get(1);
        return TileHelper.isZero(first) || TileHelper.isZero(second);
    }

    @Override
    public InstructionsAndTiming<T> generate(final MultiplyExprSymbol op, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        if(TileHelper.isZero(op.children.get(0))){
            instructions.addAll(platform.getBest(op.children.get(1)));
        }else{
            instructions.addAll(platform.getBest(op.children.get(0)));
        }
        platform.zeroDefaultLocation(instructions);
        return instructions;
    }
}
