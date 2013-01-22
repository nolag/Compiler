package cs444.generator.parser;

import java.util.List;

import cs444.generator.parser.exceptions.UndeclaredRuleException;

public class TempRule extends Rule {

    private Rule rule;

    public TempRule(String name) {
        super(name, null);
        this.firstState = RuleState.getStartState(name, name);
    }

    public void set(Rule rule){
        this.rule = rule;
        this.firstState = rule.firstState;
    }


    @Override
    public List<String> generateDcl() throws UndeclaredRuleException {
        if(null == rule) throw new UndeclaredRuleException(name);
        return rule.generateDcl();
    }

    @Override
    public List<String> generateBody() throws UndeclaredRuleException {
        if(null == rule) throw new UndeclaredRuleException(name);
        return rule.generateBody();
    }
}
