package cs444.codegen;

import cs444.codegen.generic.tiles.*;
import cs444.codegen.generic.tiles.opt.*;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.TileSet;

import java.util.Set;

public abstract class TileInit<T extends Instruction<T>, E extends Enum<E>> {
    public static final String NO_OPT = "--no-opt";
    protected final Class<? extends Platform<T, E>> klass;

    protected TileInit(Class<? extends Platform<T, E>> klass) {
        this.klass = klass;
    }

    public final void init(Set<String> options) {
        initBase();
        if (!options.contains(NO_OPT)) {
            initBasicOpt();
        }
    }

    protected void initBase() {
        TileSet<T, E> set = TileSet.getOrMake(klass);
        set.ands.add(AndTile.getTile());
        set.anonTerms.add(ANonTerminalTile.getTile());
        set.bools.add(BoolTile.getTile());
        set.constructors.add(ConstructorTile.getTile());
        set.fieldAccess.add(FieldAccessTile.getTile());
        set.fors.add(ForTile.getTile());
        set.ifs.add(IfTile.getTile());
        set.insts.add(InstanceOfTile.getTile());
        set.casts.add(LongCastTile.getTile());
        set.methods.add(MethodTile.getTile());
        set.creation.add(NormalCreationTile.getTile());
        set.nulls.add(NullTile.getTile());
        set.numbs.add(NumericalTile.getTile());
        set.ors.add(OrTile.getTile());
        set.rets.add(RetTile.getTile());
        set.invokes.add(StaticCallTile.getTile());
        set.thisables.add(ThisTile.getTile());
        set.whiles.add(WhileTile.getTile());
    }

    protected void initBasicOpt() {
        TileSet<T, E> set = TileSet.getOrMake(klass);
        set.divs.add(DivZeroTile.getTile());
        set.fieldAccess.add(NonNullFieldAccess.getTile());
        set.rems.add(RemZeroTile.getTile());
        set.casts.add(UpCastTile.getTile());
        set.mults.add(ZeroMultTile.getTile());
    }
}
