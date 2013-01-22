package cs444.generator.parser.exceptions;

public class UndeclaredRuleException extends Exception{
    private static final long serialVersionUID = 1L;

    public UndeclaredRuleException(String ruleName){
        super("Undeclared rule referenced " + ruleName);
    }
}
