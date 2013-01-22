package cs444.generator.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RuleState {

    private Rule rule;
    private Map<String, RuleState> nextStates;
    private String accepting;
    private final String name;

    //used to make unique numbers
    private static int on = 0;

    public static RuleState getStartState(String acceptingName, String name){
        return new RuleState(null, acceptingName, name);
    }

    public RuleState(Rule rule, String accepting, String name){
        this.rule = rule;
        this.accepting = accepting;
        this.name = name;
    }

    public void setNextStates(Map<String, RuleState> nextStates){
        this.nextStates = nextStates;
    }

    public Map<String, RuleState> getNextState(){
        if(null == nextStates){
            nextStates = new HashMap<String, RuleState>();
        }
        return this.nextStates;
    }

    private List<RuleState> acceptingStates(List<RuleState> acceptStates){
        if(accepting != null) acceptStates.add(this);
        for(RuleState ruleState : getNextState().values()){
            ruleState.acceptingStates(acceptStates);
        }
        return acceptStates;
    }

    public List<RuleState> acceptingStates(){
        List<RuleState> acceptStates = new LinkedList<RuleState>();
        return acceptingStates(acceptStates);
    }

    public RuleState copy(String name){
        RuleState rs = new RuleState(rule, accepting, name);
        Map<String, RuleState> nextRules = new HashMap<String, RuleState>();

        for(Entry<String, RuleState> entry : getNextState().entrySet()){
            nextRules.put(entry.getKey(), entry.getValue().copy(name));
        }
        return rs;
    }

    public boolean isAccepting(){
        return accepting != null;
    }

    public String getAccepting(){
        return accepting;
    }

    public void setAccepting(String accepting){
        this.accepting = accepting;
    }


    public void mergeWith(String name, RuleState ruleState, List<Rule> createdRules){
        if(ruleState.isAccepting()) accepting = ruleState.accepting;
        for(Entry<String, RuleState> entry : ruleState.getNextState().entrySet()){
            String key = entry.getKey();
            RuleState value = entry.getValue();
            if(!getNextState().containsKey(key)){
                getNextState().put(key, value);
                continue;
            }else{
                RuleState myNext = getNextState().get(key);
                myNext.mergeWith(name, value, createdRules);
                on++;
                myNext.rule = new ProducingRule("i_" + on + "_" + myNext.rule.name + "_" + value.rule.name, myNext);
                createdRules.add(myNext.rule);
            }

        }
    }

    public int generateRules(List<String> appendTo, int on){
        for(Entry<String, RuleState> entry: getNextState().entrySet()){
            appendTo.add(new StringBuilder("Map<Token.Type, StateTerminal> ").append(name).append(on).append(" = new HashMap<Token.Type, StateTerminal>();").toString());
            appendTo.add(new StringBuilder(name).append(on).append(".put(Token.Type.").append(entry.getKey())
                    .append(", new StateTerminal(").append(entry.getValue().name).append(", ").append(on + 1).append(")").toString());

            on = entry.getValue().generateRules(appendTo, on + 1) + 1;
        }
        return on;
    }

    public void generateRules(List<String> appendTo){
        generateRules(appendTo, 0);
    }
}
