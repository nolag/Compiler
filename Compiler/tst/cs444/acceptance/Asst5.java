package cs444.acceptance;

import org.junit.Test;

import cs444.types.PkgClassInfo;

public class Asst5 {
    @Test
    public void smallTest(){
        PkgClassInfo.instance.clear();
        System.out.println(cs444.Compiler.compile(new String []{"JoosPrograms/A5/Object.java", "JoosPrograms/A5/String.java"}, true));
    }
}
