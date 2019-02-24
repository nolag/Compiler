package cs444;

import cs444.codegen.Platform;
import cs444.codegen.Platform.PlatformFactory;
import cs444.codegen.arm.arm32.Arm32Platform;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_64.X86_64Platform;

import java.util.*;

public class CompilerSettings {
    public static final Map<String, PlatformFactory<?, ?, ?>> platformMap = new HashMap<String, PlatformFactory<?, ?,
            ?>>();

    static {
        platformMap.put("-x86", X86_32Platform.Factory.FACTORY);
        platformMap.put("-x86_32", X86_32Platform.Factory.FACTORY);
        platformMap.put("-arm", Arm32Platform.Factory.FACTORY);

        platformMap.put("-x64", X86_64Platform.Factory.FACTORY);
        platformMap.put("-x86_64", X86_64Platform.Factory.FACTORY);
        platformMap.put("-a32", Arm32Platform.Factory.FACTORY);
    }

    public final List<String> files = new ArrayList<String>();
    public final Set<Platform<?, ?>> platforms = new HashSet<Platform<?, ?>>();

    public CompilerSettings(String[] args) {
        Set<PlatformFactory<?, ?, ?>> pfs = new HashSet<PlatformFactory<?, ?, ?>>();
        Set<String> arguments = new HashSet<String>();

        for (String arg : args) {
            if (arg.startsWith("--")) {
                arguments.add(arg);
            } else if (arg.startsWith("-")) {
                PlatformFactory<?, ?, ?> platform = platformMap.get(arg.toLowerCase());
                if (platform == null) {
                    System.err.println("Unknown platfrom " + arg.substring(1));
                } else {
                    pfs.add(platform);
                }
            } else {
                files.add(arg);
            }
        }

        if (platforms.size() == 0) {
            for (String platformStr : Compiler.defaultPlatforms) {
                platforms.add(platformMap.get(platformStr).getPlatform(arguments));
            }
        } else {
            for (PlatformFactory<?, ?, ?> pf : pfs) {
                platforms.add(pf.getPlatform(arguments));
            }
        }
    }
}