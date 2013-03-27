package cs444.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SelectorIndexedTable {
    private final Map<String, Map<String, String>> sit = new HashMap<String, Map<String,String>>();;
    private final Set<String> selectors = new HashSet<String>();

    public void addIndex(String className, String selector,
            String methodImplLabel) {

        Map<String, String> column = sit.get(className);
        if (column == null){
            column = new HashMap<String, String>();
            sit.put(className, column);
        }

        column.put(selector, methodImplLabel);
        selectors.add(selector);
    }
}
