package cs444.generator.parser;

import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import cs444.generator.lexer.grammar.JoosGrammar;

public class JoosSyntacticGrammar extends Language {

    private static final JoosGrammar tokenGrammar = new JoosGrammar();
    private static final String startRule = "CompilationUnit";
    private static final List<String> rules = new LinkedList<String>();

    static{
        // rules.add("CompilationUnit PackageDeclaration EOF");
        // rules.add("PackageDeclaration package id semi");

        // CompilationUnit:
        // PackageDeclaration_opt ImportDeclarations_opt TypeDeclarations_opt
        rules.add("CompilationUnit TypeDeclarations");
        rules.add("CompilationUnit ImportDeclarations");
        rules.add("CompilationUnit ImportDeclarations TypeDeclarations");
        rules.add("CompilationUnit PackageDeclaration");
        rules.add("CompilationUnit PackageDeclaration TypeDeclarations");
        rules.add("CompilationUnit PackageDeclaration ImportDeclarations");
        rules.add("CompilationUnit PackageDeclaration ImportDeclarations TypeDeclarations");

        // ImportDeclarations:
        // ImportDeclaration
        // ImportDeclarations ImportDeclaration
        rules.add("ImportDeclarations ImportDeclaration");
        rules.add("ImportDeclarations ImportDeclarations ImportDeclaration");

        // TypeDeclarations:
        // TypeDeclaration
        // TypeDeclarations TypeDeclaration
        rules.add("TypeDeclarations TypeDeclaration");
        rules.add("TypeDeclarations TypeDeclarations TypeDeclaration");

        rules.add("PackageDeclaration package id semi");

        // ImportDeclaration:
        // SingleTypeImportDeclaration
        // TypeImportOnDemandDeclaration
        rules.add("ImportDeclaration SingleTypeImportDeclaration");
        rules.add("ImportDeclaration TypeImportOnDemandDeclaration");

        // SingleTypeImportDeclaration:
        // import TypeName
        rules.add("SingleTypeImportDeclaration import id semi");

        // TypeImportOnDemandDeclaration:
        // import PackageOrTypeName . * ;
        rules.add("TypeImportOnDemandDeclaration import id dot star semi");

        // TypeDeclaration:
        // ClassDeclaration
        // InterfaceDeclaration
        // ;
        rules.add("TypeDeclaration ClassDeclaration");
        rules.add("TypeDeclaration InterfaceDeclaration");
        rules.add("TypeDeclaration semi");

        // ClassDeclaration:
        // ClassModifiers_opt class Identifier Super_opt Interfaces_opt ClassBody
        rules.add("ClassDeclaration class id ClassBody");
        rules.add("ClassDeclaration class id Interfaces ClassBody");
        rules.add("ClassDeclaration class id Super ClassBody");
        rules.add("ClassDeclaration class id Super Interfaces ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id Interfaces ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id Super ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id Super Interfaces ClassBody");

        // ClassModifiers:
        // ClassModifier
        // ClassModifiers ClassModifier
        // ClassModifier: one of: public protected private abstract static final strictfp
        rules.add("ClassModifiers ClassModifier");
        rules.add("ClassModifiers ClassModifiers ClassModifier");
        rules.add("ClassModifier public");
        rules.add("ClassModifier protected");
        rules.add("ClassModifier private");
        rules.add("ClassModifier abstract");
        rules.add("ClassModifier static");
        rules.add("ClassModifier final");
        // TODO: this is not required by Joos
        rules.add("ClassModifier strictfp");

        // Super:
        // extends ClassType
        rules.add("Super extends id");

    }

    JoosSyntacticGrammar(Writer writer){
        super(writer, rules, startRule, tokenGrammar, "JoosRules");
    }
}
