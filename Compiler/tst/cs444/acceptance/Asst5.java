package cs444.acceptance;

import org.junit.Test;

import cs444.types.PkgClassInfo;

public class Asst5 {
    @Test
    public void smallTest(){
        PkgClassInfo.instance.clear();
        System.out.println(cs444.Compiler.compile(new String []{"JoosPrograms/A5/Test1/Object.java", "JoosPrograms/A5/Test1/String.java"}, true, true));
    }

    @Test
    public void ifBoolAndMath(){
        PkgClassInfo.instance.clear();
        System.out.println(cs444.Compiler.compile(new String []{"JoosPrograms/A5/Test2/Object.java", "JoosPrograms/A5/Test2/String.java"}, true, true));
    }
}
