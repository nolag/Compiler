package cs444.generator.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cs444.generator.parser.exceptions.UndeclaredRuleException;

public class TerminalRule extends Rule{

    //Terminals don't have a body for the decleration
    private static List<String> body = new LinkedList<String>();

	private TerminalRule(String name, RuleState rule) {
	    super(name, rule);
	}

	public static TerminalRule create(String name){
	    RuleState rule = RuleState.getStartState(null, name);
	    TerminalRule literal = new TerminalRule(name, rule);
	    Map<String, RuleState> nextStates = new HashMap<String, RuleState>();
	    nextStates.put(name, new RuleState(literal, name, name));
	    rule.setNextStates(nextStates);
	    return literal;
	}

    @Override
    public List<String> generateDcl(){
        //_ because some of these are these are keywords
        return Arrays.asList(new String[]{"TerminalFactory _" + name + " = new TerminalFactory(Token.Type." + name.toUpperCase() + ");" });
    }

    @Override
    public List<String> generateBody() throws UndeclaredRuleException {
        return body;
    }
}
