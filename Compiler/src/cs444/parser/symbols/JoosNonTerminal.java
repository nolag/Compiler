package cs444.parser.symbols;

import java.util.*;

import cs444.CompilerException;
import cs444.ast.ISymbolVisitor;
import cs444.codegen.CodeGenVisitor;
import cs444.types.APkgClassResolver;

public class JoosNonTerminal extends NonTerminal{
    public static final String BRACKET_PRIMARY = "BRACKETPRIMARY";
    public static final String ARRAY_TYPE = "ARRAYTYPE";
    public static final String TYPE = "TYPE";
    public static final String IF_THEN_STATEMENT = "IFTHENSTATEMENT";
    public static final String FOR_UPDATE = "FORUPDATE";
    public static final String FOR_INIT = "FORINIT";
    public static final String DIMS = "DIMS";
    public static final String ARGUMENT_LIST = "ARGUMENTLIST";
    public static final String DIM_EXPR = "DIMEXPR";
    public static final String BLOCK = "BLOCK";

    public static final String UNARY_EXPRESSION = "UNARYEXPRESSION";
    public static final String UNARY_EXPRESSION_NOT_PLUS_MINUS = "UNARYEXPRESSIONNOTPLUSMINUS";

    public static final String METHOD_INVOCATION = "METHODINVOCATION";
    public static final String ASSIGNMENT = "ASSIGNMENT";

    public static final String RETURN = "RETURNSTATEMENT";
    public static final String EMPTYSTATEMENT= "EMPTYSTATEMENT";
    public static final String ARRAY_ACCESS = "ARRAYACCESS";
    public static final String NULL = "null";
    public static final String SUPER = "super";
    public static final String THIS = "this";
    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";
    public static final String BOOLEAN = "boolean";
    public static final String VOID = "void";

    public static final String LANG = "java.lang";

    public static final String OBJECT = LANG + ".Object";
    public static final String CLONABLE = LANG + ".Cloneable";
    public static final String STRING = LANG + ".String";

    public static final String IO = "java.io";
    public static final String SERIALIZABLE = IO + ".Serializable";

    public static final String LONG = "long";
    public static final String INTEGER = "int";
    public static final String SHORT = "short";
    public static final String CHAR = "char";
    public static final String BYTE = "byte";

    public static final String STR_ADD = "concat";
    public static final String TO_STR = "valueOf";
    public static final String LENGTH = "length";

    public static final String DIV_ZERO = "java.lang.ArithmeticException: / by zero";

    public static final Map<String, Set<String>> defaultAssignables = new HashMap<String, Set<String>>();
    public static final Map<String, Set<String>> specialAssignables = new HashMap<String, Set<String>>();

    public static final Set<String> whiles = new HashSet<String>();
    public static final Set<String> notAllowedForInstanceOfLHS = new HashSet<String>();
    public static final Set<String> notAllowedForInstanceOfRHS = new HashSet<String>();

    public static final Set<String> fors = new HashSet<String>();
    public static final Set<String> ifs = new HashSet<String>();

    public static final Set<String> unaryExpressions = new HashSet<String>();
    public static final Set<String> binExpressions = new HashSet<String>();

    public static final Set<String> noCollapse = new HashSet<String>();
    public static final Set<String> specialNoDie = new HashSet<String>();
    public static final Set<String> primativeNumbers = new HashSet<String>();
    public static final Set<String> otherPrimatives = new HashSet<String>();

    public static final Set<String> nonPrimativeOperativeTypes = new HashSet<String>();

    public static final List<String> arraysExtend = new LinkedList<String>();

    public static final Set<String> unsigned = new HashSet<String>();

    public static final String ENTRY = APkgClassResolver.generateUniqueName("test", Collections.<String>emptyList());

    static{
        noCollapse.add("MODIFIERS");
        noCollapse.add("NAME");
        noCollapse.add("INTERFACETYPELIST");
        noCollapse.add("CLASSTYPE");
        noCollapse.add("TYPEDECLARATION");
        noCollapse.add(TYPE);
        noCollapse.add(ARRAY_TYPE);
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
        noCollapse.add(BRACKET_PRIMARY);
        noCollapse.add(METHOD_INVOCATION);
        noCollapse.add("IMPORTDECLARATIONS");
        noCollapse.add(BLOCK);
        noCollapse.add(DIM_EXPR);
        noCollapse.add(ARGUMENT_LIST);
        noCollapse.add(RETURN);
        noCollapse.add(FOR_INIT);
        noCollapse.add(FOR_UPDATE);
        noCollapse.add(ASSIGNMENT);

        specialNoDie.add("EMPTYSTATEMENT");
        specialNoDie.add(BLOCK);
        specialNoDie.add(RETURN);
        specialNoDie.add(DIMS);


        primativeNumbers.add("byte");
        primativeNumbers.add(CHAR);
        primativeNumbers.add("short");
        primativeNumbers.add(INTEGER);
        notAllowedForInstanceOfLHS.addAll(primativeNumbers);

        otherPrimatives.add(VOID);
        otherPrimatives.add(BOOLEAN);
        notAllowedForInstanceOfLHS.addAll(otherPrimatives);

        //null is special, so it is allowed for insance of
        otherPrimatives.add(NULL);
        notAllowedForInstanceOfLHS.addAll(notAllowedForInstanceOfLHS);
        notAllowedForInstanceOfRHS.add(NULL);

        unaryExpressions.add("POSTFIXEXPRESSION");
        unaryExpressions.add(UNARY_EXPRESSION);
        unaryExpressions.add(UNARY_EXPRESSION_NOT_PLUS_MINUS);

        binExpressions.add("MULTIPLICATIVEEXPRESSION");
        binExpressions.add("ADDITIVEEXPRESSION");
        binExpressions.add("RELATIONALEXPRESSION");
        binExpressions.add("EQUALITYEXPRESSION");
        binExpressions.add("ASSIGNMENTEXPRESSION");
        binExpressions.add("CONDITIONALANDEXPRESSION");
        binExpressions.add("INCLUSIVEOREXPRESSION");
        binExpressions.add("CONDITIONALOREXPRESSION");
        binExpressions.add("ASSIGNMENT");
        binExpressions.add("ANDEXPRESSION");
        binExpressions.add("SHIFTEXPRESSION");

        fors.add("FORSTATEMENT");
        fors.add("FORSTATEMENTNOSHORTIF");

        ifs.add(IF_THEN_STATEMENT);
        ifs.add("IFTHENELSESTATEMENT");
        ifs.add("IFTHENELSESTATEMENTNOSHORTIF");

        final Set<String> byteCharAssign = new HashSet<String>(Arrays.asList(new String [] {INTEGER, SHORT}));
        final Set<String> assignToint = new HashSet<String>(Arrays.asList(new String [] {INTEGER}));
        defaultAssignables.put(BYTE, byteCharAssign);
        defaultAssignables.put(CHAR, assignToint);
        defaultAssignables.put(SHORT, assignToint);

        final Set<String> alsoToShort = new HashSet<String>(Arrays.asList(new String [] {CHAR}));
        final Set<String> alsoToChar = new HashSet<String>(Arrays.asList(new String [] {SHORT}));
        specialAssignables.put(SHORT, alsoToShort);
        specialAssignables.put(CHAR, alsoToChar);
        specialAssignables.put(BYTE, alsoToShort);

        whiles.add("WHILESTATEMENT");
        whiles.add("WHILESTATEMENTNOSHORTIF");

        nonPrimativeOperativeTypes.add(STRING);
        arraysExtend.add(JoosNonTerminal.CLONABLE);
        arraysExtend.add(JoosNonTerminal.SERIALIZABLE);

        unsigned.add(CHAR);
        unsigned.add(BOOLEAN);
    }

    public JoosNonTerminal(final String name, final ISymbol[] children) {
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
    public void accept(final ISymbolVisitor visitor) throws CompilerException {
        visitor.visit(this);
        visitor.open(this);
        for (final ISymbol child : this.children) {
            child.accept(visitor);
        }
        visitor.close(this);
    }

    @Override
    public void accept(final CodeGenVisitor visitor) {
        visitor.visit(this);
    }
}
