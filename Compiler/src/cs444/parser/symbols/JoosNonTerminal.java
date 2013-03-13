package cs444.parser.symbols;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;

public class JoosNonTerminal extends NonTerminal{
    public static final String DIMS = "DIMS";
    public static final String ARGUMENT_LIST = "ARGUMENTLIST";
    public static final String DIM_EXPR = "DIMEXPR";
    public static final String BLOCK = "BLOCK";

    public static final String UNARY_EXPRESSION = "UNARYEXPRESSION";

    public static final String METHOD_INVOCATION = "METHODINVOCATION";

    public static final String RETURN = "RETURNSTATEMENT";
    public static final String NULL = "null";
    public static final String SUPER = "super";
    public static final String THIS = "this";
    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "true";
    public static final String BOOLEAN = "boolean";
    public static final String STRING = "java.lang.String";
    public static final String CHAR = "char";
    public static final String INTEGER = "int";

    public static final Map<String, List<String>> defaultAssignables = new HashMap<String, List<String>>();

    public static final Set<String> whiles = new HashSet<String>();

    public static final Set<String> unaryExpressions = new HashSet<String>();
    public static final Set<String> binExpressions = new HashSet<String>();

    public static final Set<String> noCollapse = new HashSet<String>();
    public static final Set<String> specialNoDie = new HashSet<String>();
    public static final Set<String> primativeNumbers = new HashSet<String>();
    public static final Set<String> otherPrimatives = new HashSet<String>();

    static{
        noCollapse.add("MODIFIERS");
        noCollapse.add("NAME");
        noCollapse.add("INTERFACETYPELIST");
        noCollapse.add("CLASSTYPE");
        noCollapse.add("TYPEDECLARATION");
        noCollapse.add("TYPE");
        noCollapse.add("ARRAYTYPE");
        noCollapse.add("VARIABLEINITIALIZER");
        noCollapse.add("VARIABLEDECLARATOR");
        noCollapse.add("CONSTRUCTORDECLARATION");
        noCollapse.add("FORMALPARAMETERLIST");
        noCollapse.add("CONSTRUCTORBODY");
        noCollapse.add("METHODBODY");
        noCollapse.add("METHODDECLARATION");
        noCollapse.add("ABSTRACTMETHODDECLARATION");
        noCollapse.add("METHODDECLARATOR");
        noCollapse.add("CONSTRUCTORDECLARATOR");
        noCollapse.add("INTERFACEMEMBERDECLARATIONS");
        noCollapse.add("CLASSBODYDECLARATIONS");
        noCollapse.add("BRACKETPRIMARY");
        noCollapse.add(METHOD_INVOCATION);
        noCollapse.add("IMPORTDECLARATIONS");
        noCollapse.add(BLOCK);
        noCollapse.add(DIM_EXPR);
        noCollapse.add(ARGUMENT_LIST);
        noCollapse.add(RETURN);

        specialNoDie.add("EMPTYSTATEMENT");
        specialNoDie.add(BLOCK);
        specialNoDie.add(RETURN);
        specialNoDie.add(DIMS);


        primativeNumbers.add("byte");
        primativeNumbers.add("char");
        primativeNumbers.add("short");
        primativeNumbers.add("int");

        otherPrimatives.add("void");
        otherPrimatives.add("boolean");
        otherPrimatives.add("null");

        unaryExpressions.add("POSTFIXEXPRESSION");
        unaryExpressions.add(UNARY_EXPRESSION);

        binExpressions.add("MULTIPLICATIVEEXPRESSION");
        binExpressions.add("ADDITIVEEXPRESSION");
        binExpressions.add("RELATIONALEXPRESSION");
        binExpressions.add("EQUALITYEXPRESSION");
        binExpressions.add("ASSIGNMENTEXPRESSION");
        binExpressions.add("CONDITIONALANDEXPRESSION");
        binExpressions.add("INCLUSIVEOREXPRESSION");

        List<String> byteCharAssign = Arrays.asList(new String [] {"int", "short"});
        List<String> shortAssign = Arrays.asList(new String [] {"int"});
        defaultAssignables.put("byte", byteCharAssign);
        defaultAssignables.put("char", byteCharAssign);
        defaultAssignables.put("short", shortAssign);
    }

    public JoosNonTerminal(String name, ISymbol[] children) {
        super(name, children);
    }

    @Override
    public Set<String> noCollapse() {
        return noCollapse;
    }

    @Override
    public boolean empty() {
        return children.size() == 0 && !specialNoDie.contains(name.toUpperCase());
    }

    @Override
    public void accept(ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
        visitor.open(this);
        for (ISymbol child : this.children) {
            child.accept(visitor);
        }
        visitor.close(this);
    }
}
