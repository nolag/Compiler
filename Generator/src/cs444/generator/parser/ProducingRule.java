package cs444.generator.parser;

import java.util.LinkedList;
import java.util.List;

import cs444.generator.parser.exceptions.UndeclaredRuleException;


public class ProducingRule extends Rule{

	public ProducingRule(String name, RuleState firstState) {
		super(name, firstState);
	}

    @Override
    public List<String> generateDcl(){
        List<String> lines = new LinkedList<String>();
        lines.add(new StringBuilder("Map<Integer, Map<Token.Type, StateTerminal>>").append(name)
                .append("Rules = new HashMap<Integer, Map<Token.Type, StateTerminal>>();").toString());

        lines.add(new StringBuilder("Map<Integer, String> ").append(name)
                .append("Accepting = new HashMap<Integer, String>();").toString());

        lines.add(new StringBuilder("NonTerminalFactory ").append(name).append(" = new NonTerminalFactory(")
                .append(name).append("Rules").append(", ").append(name).append("Accepting, \"").append(name).append("\");").toString());

        return lines;
    }

    @Override
    public List<String> generateBody() throws UndeclaredRuleException {
        List<String> body = new LinkedList<String>();
        firstState.generateRules(body);
        return body;
    }
}
