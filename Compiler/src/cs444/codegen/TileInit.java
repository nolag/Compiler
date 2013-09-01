package cs444.codegen;

import java.util.Set;

public abstract class TileInit {
    protected static final String NO_OPT = "--no-opt";

    public void init(final Set<String> options){
        initBase();
        if(!options.contains(NO_OPT)){
            initBasicOpt();
        }
    }

    protected abstract void initBase();
    protected abstract void initBasicOpt();
}
