package cs444.codegen;

import java.util.Set;

import cs444.codegen.generic.tiles.AndTile;
import cs444.codegen.generic.tiles.BoolTile;
import cs444.codegen.generic.tiles.FieldAccessTile;
import cs444.codegen.generic.tiles.IfTile;
import cs444.codegen.generic.tiles.InstanceOfTile;
import cs444.codegen.generic.tiles.MethodTile;
import cs444.codegen.generic.tiles.OrTile;
import cs444.codegen.generic.tiles.WhileTile;
import cs444.codegen.generic.tiles.opt.DivZeroTile;
import cs444.codegen.generic.tiles.opt.NonNullFieldAccess;
import cs444.codegen.generic.tiles.opt.RemZeroTile;
import cs444.codegen.generic.tiles.opt.UpCastTile;
import cs444.codegen.generic.tiles.opt.ZeroMultTile;
import cs444.codegen.instructions.Instruction;

public abstract class TileInit <T extends Instruction<T>, E extends Enum<E>> {
    protected final Class<? extends Platform<T, E>> klass;
    
    protected TileInit(final Class<? extends Platform<T, E>> klass) {
        this.klass = klass;
    }
    
    public static final String NO_OPT = "--no-opt";

    public void init(final Set<String> options){
        initBase();
        if(!options.contains(NO_OPT)){
            initBasicOpt();
        }
    }

    protected void initBase() {
        AndTile.<T, E>init(klass);
        BoolTile.<T, E>init(klass);
        FieldAccessTile.<T, E>init(klass);
        IfTile.<T, E>init(klass);
        InstanceOfTile.<T, E>init(klass);
        MethodTile.<T, E>init(klass);
        OrTile.<T, E>init(klass);
        WhileTile.<T, E>init(klass);
    }
    
    protected void initBasicOpt() {
        DivZeroTile.<T, E>init(klass);
        NonNullFieldAccess.<T, E>init(klass);
        RemZeroTile.<T, E>init(klass);
        UpCastTile.<T, E>init(klass);
        ZeroMultTile.<T, E>init(klass);
    }
}
