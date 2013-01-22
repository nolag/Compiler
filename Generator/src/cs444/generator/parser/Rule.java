package cs444.generator.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cs444.generator.parser.exceptions.UndeclaredRuleException;

public abstract class Rule{
	public final String name;

	protected RuleState firstState;

	protected Rule(String name, final RuleState firstState){
		this.name = name;
		this.firstState = firstState;
	}

	private static List<Rule> union(String name, boolean merging, Rule ... rules){
	    List<Rule> createdRules = new LinkedList<Rule>();
	    RuleState start = RuleState.getStartState(null, name);
        for(Rule rule : rules){
            RuleState clone = rule.firstState.copy(name);
            start.mergeWith(name, clone, createdRules);
        }

        createdRules.add(new ProducingRule(name, start));
        return createdRules;
	}

	public static List<Rule> union(String name, Rule ... rules){
	    return union(name, false, rules);
	}

	public RuleState firstStates(){
	    return firstState;
	}

	public static List<Rule> concatinate(String name, Rule ... rules){
	    List<Rule> ruleList = new LinkedList<Rule>();
	    RuleState start = RuleState.getStartState(null, name);
	    List<RuleState> accepts = Arrays.asList(new RuleState []{ start });

	    for(Rule rule : rules){
	        RuleState clone = rule.firstState.copy(name);

	        for(RuleState accept : accepts){
	            if(!clone.isAccepting()){
	                accept.setAccepting(null);
	            }

	            if(accept.getNextState() == null) accept.setNextStates(clone.getNextState());
	            else accept.mergeWith(name, clone, ruleList);
	        }

	        if(!clone.isAccepting()){
	            accepts = clone.acceptingStates();
	        }else{
	            accepts.addAll(clone.acceptingStates());
	        }
	    }
	    ruleList.add(new ProducingRule(name, start));
	    return ruleList;
	}

	public abstract List<String> generateDcl() throws UndeclaredRuleException;
	public abstract List<String> generateBody() throws UndeclaredRuleException;
}
