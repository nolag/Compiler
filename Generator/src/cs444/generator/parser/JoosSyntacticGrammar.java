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

        rules.add("S CompilationUnit EOF");
        rules.add("S EOF"); // Empty file

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

        
    }

    JoosSyntacticGrammar(Writer writer){
        super(writer, rules, startRule, tokenGrammar, "JoosRules");
    }
}
