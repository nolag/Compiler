package cs444.codegen;

import java.util.Set;

import cs444.codegen.generic.tiles.*;
import cs444.codegen.generic.tiles.opt.DivZeroTile;
import cs444.codegen.generic.tiles.opt.NonNullFieldAccess;
import cs444.codegen.generic.tiles.opt.RemZeroTile;
import cs444.codegen.generic.tiles.opt.UpCastTile;
import cs444.codegen.generic.tiles.opt.ZeroMultTile;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.TileSet;

public abstract class TileInit<T extends Instruction<T>, E extends Enum<E>> {
    protected final Class<? extends Platform<T, E>> klass;

    protected TileInit(final Class<? extends Platform<T, E>> klass) {
        this.klass = klass;
    }

    public static final String NO_OPT = "--no-opt";

    public final void init(final Set<String> options) {
        initBase();
        if (!options.contains(NO_OPT)) {
            initBasicOpt();
        }
    }

    protected void initBase() {
        final TileSet<T, E> set = TileSet.<T, E> getOrMake(klass);
        set.ands.add(AndTile.<T, E> getTile());
        set.anonTerms.add(ANonTerminalTile.<T, E> getTile());
        set.bools.add(BoolTile.<T, E> getTile());
        set.constructors.add(ConstructorTile.<T, E> getTile());
        set.fieldAccess.add(FieldAccessTile.<T, E> getTile());
        set.fors.add(ForTile.<T, E> getTile());
        set.ifs.add(IfTile.<T, E> getTile());
        set.insts.add(InstanceOfTile.<T, E> getTile());
        set.casts.add(LongCastTile.<T, E> getTile());
        set.methods.add(MethodTile.<T, E> getTile());
        set.creation.add(NormalCreationTile.<T, E> getTile());
        set.nulls.add(NullTile.<T, E> getTile());
        set.numbs.add(NumericalTile.<T, E> getTile());
        set.ors.add(OrTile.<T, E> getTile());
        set.rets.add(RetTile.<T, E> getTile());
        set.invokes.add(StaticCallTile.<T, E> getTile());
        set.thisables.add(ThisTile.<T, E> getTile());
        set.whiles.add(WhileTile.<T, E> getTile());
    }

    protected void initBasicOpt() {
        final TileSet<T, E> set = TileSet.<T, E> getOrMake(klass);
        set.divs.add(DivZeroTile.<T, E> getTile());
        set.fieldAccess.add(NonNullFieldAccess.<T, E> getTile());
        set.rems.add(RemZeroTile.<T, E> getTile());
        set.casts.add(UpCastTile.<T, E> getTile());
        set.mults.add(ZeroMultTile.<T, E> getTile());
    }
}
