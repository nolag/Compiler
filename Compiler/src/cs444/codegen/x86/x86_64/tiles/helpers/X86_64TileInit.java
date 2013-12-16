package cs444.codegen.x86.x86_64.tiles.helpers;

import cs444.codegen.x86.tiles.helpers.X86TileInit;
import cs444.codegen.x86.x86_64.X86_64Platform;
import cs444.codegen.x86.x86_64.tiles.StaticNameValue;

public class X86_64TileInit extends X86TileInit{
    public static final X86_64TileInit instance = new X86_64TileInit();

    private X86_64TileInit(){
        super(X86_64Platform.class);
    }
    
    @Override
    public void initBase(){
        super.initBase();
        StaticNameValue.init();
    }
}
