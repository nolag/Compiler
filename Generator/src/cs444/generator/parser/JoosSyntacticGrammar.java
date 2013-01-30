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
        // *** Names ***
        // PackageName:
        // Identifier
        // PackageName . Identifier
        rules.add("PackageName id");
        rules.add("PackageName PackageName dot id");

        // TypeName:
        // Identifier
        // PackageOrTypeName . Identifier
        rules.add("TypeName id");
        rules.add("TypeName PackageOrTypeName dot id");

        // ExpressionName:
        // Identifier
        // AmbiguousName . Identifier
        rules.add("ExpressionName id");
        rules.add("ExpressionName AmbiguousName dot id");

        // MethodName:
        // Identifier
        // AmbiguousName . Identifier
        rules.add("MethodName id");
        rules.add("MethodName AmbiguousName dot id");

        // PackageOrTypeName:
        // Identifier
        // PackageOrTypeName . Identifier
        rules.add("PackageOrTypeName id");
        rules.add("PackageOrTypeName PackageOrTypeName dot id");

        // AmbiguousName:
        // Identifier
        // AmbiguousName . Identifier
        rules.add("AmbiguousName id");
        rules.add("AmbiguousName AmbiguousName dot id");

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
        rules.add("PackageDeclaration package PackageName semi");

        // ImportDeclaration:
        // SingleTypeImportDeclaration
        // TypeImportOnDemandDeclaration
        rules.add("ImportDeclaration SingleTypeImportDeclaration");
        rules.add("ImportDeclaration TypeImportOnDemandDeclaration");

        // SingleTypeImportDeclaration:
        // import TypeName
        rules.add("SingleTypeImportDeclaration import TypeName semi");

        // TypeImportOnDemandDeclaration:
        // import PackageOrTypeName . * ;
        rules.add("TypeImportOnDemandDeclaration import PackageOrTypeName dot star semi");

        // TypeDeclaration:
        // ClassDeclaration
        // InterfaceDeclaration
        // ;
        rules.add("TypeDeclaration ClassDeclaration");
 // TODO: uncomment this after adding rules for InterfaceDeclaration
 // rules.add("TypeDeclaration InterfaceDeclaration");
        rules.add("TypeDeclaration semi");

        // ClassDeclaration:
        // ClassModifiers_opt class Identifier Super_opt Interfaces_opt ClassBody
        rules.add("ClassDeclaration class id ClassBody");
        rules.add("ClassDeclaration class id Interfaces ClassBody");
        rules.add("ClassDeclaration class id SuperClass ClassBody");
        rules.add("ClassDeclaration class id SuperClass Interfaces ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id Interfaces ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id SuperClass ClassBody");
        rules.add("ClassDeclaration ClassModifiers class id SuperClass Interfaces ClassBody");

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
        rules.add("SuperClass extends ClassType");
        rules.add("ClassType TypeName");

        // Interfaces:
        // implements InterfaceTypeList
        rules.add("Interfaces implements InterfaceTypeList");

        // InterfaceTypeList:
        // InterfaceType
        // InterfaceTypeList , InterfaceType
        rules.add("InterfaceTypeList InterfaceType");
        rules.add("InterfaceTypeList InterfaceTypeList comma InterfaceType");
        rules.add("InterfaceType TypeName");

        // ClassBody:
        // { ClassBodyDeclarations_opt }
        rules.add("ClassBody lbrace rbrace");
        // rules.add("ClassBody LBRACE ClassBodyDeclarations RBRACE");

        // // ClassBodyDeclarations:
        // // ClassBodyDeclaration
        // // ClassBodyDeclarations ClassBodyDeclaration
        // rules.add("ClassBodyDeclarations ClassBodyDeclaration");
        // rules.add("ClassBodyDeclarations ClassBodyDeclarations ClassBodyDeclaration");

        // // ClassBodyDeclaration:
        // // ClassMemberDeclaration
        // // InstanceInitializer
        // // StaticInitializer
        // // ConstructorDeclaration
        // rules.add("ClassBodyDeclaration ClassMemberDeclaration");
        // rules.add("ClassBodyDeclaration InstanceInitializer");
        // rules.add("ClassBodyDeclaration StaticInitializer");
        // rules.add("ClassBodyDeclaration ConstructorDeclaration");

        // // ClassMemberDeclaration:
        // // FieldDeclaration
        // // MethodDeclaration
        // // ClassDeclaration
        // // InterfaceDeclaration
        // // ;
        // rules.add("ClassMemberDeclaration FieldDeclaration");
        // rules.add("ClassMemberDeclaration MethodDeclaration");
        // rules.add("ClassMemberDeclaration ClassDeclaration");
        // rules.add("ClassMemberDeclaration InterfaceDeclaration");
        // rules.add("ClassMemberDeclaration semi");

        // // FieldDeclaration:
        // // FieldModifiers_opt Type VariableDeclarators ;
        // rules.add("FieldDeclaration Type VariableDeclarators semi");
        // rules.add("FieldDeclaration FieldModifiers Type VariableDeclarators semi");

        // // VariableDeclarators:
        // // VariableDeclarator
        // // VariableDeclarators , VariableDeclarator
        // rules.add("VariableDeclarators VariableDeclarator");
        // rules.add("VariableDeclarators VariableDeclarators semi Declarator");

        // // VariableDeclarator:
        // // VariableDeclaratorId
        // // VariableDeclaratorId = VariableInitializer
        // rules.add("VariableDeclarator VariableDeclaratorId");
        // // VariableDeclaratorId = VariableInitializer

        // VariableDeclaratorId:
        // Identifier
        // VariableDeclaratorId [ ]
        // VariableInitializer:
        // Expression
        // ArrayInitializer
    }

    public JoosSyntacticGrammar(Writer writer){
        super(writer, rules, startRule, tokenGrammar, "JoosDFA");
    }
}
