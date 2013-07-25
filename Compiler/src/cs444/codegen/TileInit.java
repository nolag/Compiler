package cs444.codegen;

import java.util.Map;

public abstract class TileInit {
    protected static final String NO_OPT = "--no-opt";

    public void init(final Map<String, Boolean> options){
        initBase();
        if(!options.containsKey(NO_OPT)){
            initBasicOpt();
        }
    }

    protected abstract void initBase();
    protected abstract void initBasicOpt();
}
