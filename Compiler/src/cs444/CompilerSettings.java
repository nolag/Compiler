package cs444;

import java.util.*;

import cs444.codegen.Platform;
import cs444.codegen.Platform.PlatformFactory;
import cs444.codegen.x86.x86_32.linux.X86_32LinuxPlatform;

public class CompilerSettings {
    public static final Map<String, PlatformFactory<?, ?, ?>> platformMap = new HashMap<String, PlatformFactory<?, ?, ?>>();

    static{
        platformMap.put("-x86linux", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
        platformMap.put("-x86l", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
        platformMap.put("-x86_32linux", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
        platformMap.put("-x86_32l", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);

        platformMap.put("-x64linux", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
        platformMap.put("-x64l", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
        platformMap.put("-x86_64linux", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
        platformMap.put("-x86_64l", X86_32LinuxPlatform.X86_32LinuxPlatformFactory.factory);
    }

    public final List<String> files = new ArrayList<String>();
    public final Set<Platform<?, ?>> platforms = new HashSet<Platform<?, ?>>();

    public CompilerSettings(final String [] args){
        final Set<PlatformFactory<?, ?, ?>> pfs = new HashSet<PlatformFactory<?, ?, ?>>();
        final Set<String> arguments = new HashSet<String>();

        for(final String arg : args){
            if(arg.startsWith("--")){
                arguments.add(arg);
            }else if(arg.startsWith("-")){
                final PlatformFactory<?, ?, ?> platform = platformMap.get(arg.toLowerCase());
                if(platform == null) System.err.println("Unknown platfrom " + arg.substring(1));
                else pfs.add(platform);
            }else{
                files.add(arg);
            }
        }

        if(platforms.size() == 0){
            for(final String platformStr : Compiler.defaultPlatforms){
                platforms.add(platformMap.get(platformStr).getPlatform(arguments));
            }
        }else{
            for(final PlatformFactory<?, ?, ?> pf : pfs) platforms.add(pf.getPlatform(arguments));
        }

    }
}
