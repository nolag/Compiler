package cs444;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.SelectorIndexedTable;
import cs444.codegen.StaticFieldInit;
import cs444.codegen.SubtypeIndexedTable;
import cs444.lexer.Lexer;
import cs444.lexer.LexerException;
import cs444.parser.IASTBuilder;
import cs444.parser.JoosASTBuilder;
import cs444.parser.Parser;
import cs444.parser.TextReadingRules;
import cs444.parser.symbols.ANonTerminal;
import cs444.parser.symbols.ast.AInterfaceOrClassSymbol;
import cs444.parser.symbols.exceptions.UnexpectedTokenException;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassInfo;

public class Compiler {
    //public static final String BASE_DIRECTORY = "/mnt/hgfs/RAM/";
	//public static final String BASE_DIRECTORY = "E:/RAM/";
    public static final String BASE_DIRECTORY = "";
    public static final String OUTPUT_DIRECTORY = BASE_DIRECTORY + "output/";

    public static final int COMPILER_ERROR_CODE = 42;

    /**
     * @param args
     * @throws URISyntaxException
     * @throws Exception
     */
    public static void main(String[] args){
        System.exit(compile(args, true, true));
    }

    public static int compile(String[] files, boolean printErrors, boolean outputFiles) {
        if (files.length == 0){
            System.err.println("ERROR: At least a file should be passed.");
            printUsage();
            return COMPILER_ERROR_CODE;
        }

        Reader reader = null;
        ANonTerminal parseTree = null;

        try {
            for(String fileName : files){

                reader = new FileReader(fileName);
                parseTree = parse(reader);

                IASTBuilder builder = new JoosASTBuilder(new File(fileName).getName());
                parseTree = (ANonTerminal)builder.build(parseTree);

                PkgClassInfo.instance.addClassOrInterface((AInterfaceOrClassSymbol)parseTree);
            }

            //Make a copy, the symbols can add more for arrays.
            List<APkgClassResolver> resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());
            buildAllResolvers(resolvers);

            resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());
            analyzeReachability(resolvers);

            resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());
            typeCheck(resolvers);

            resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());
            checkFields(resolvers);

            resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());
            generateCode(resolvers, outputFiles);

        }catch(Exception e){
            if (printErrors) e.printStackTrace();
            return COMPILER_ERROR_CODE;
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    if (printErrors) e.printStackTrace();
                }
            }
        }

        return 0;
    }

    private static void analyzeReachability(List<APkgClassResolver> resolvers)
            throws CompilerException {

        for (APkgClassResolver resolver : resolvers) {
            resolver.reduceToConstantExprs();
            resolver.analyzeReachability();
        }
    }

    private static void checkFields(List<APkgClassResolver> resolvers)
            throws CompilerException {
        //Do field init here
        for(APkgClassResolver resolver : resolvers){
            resolver.checkFields();
        }
    }

    private static void typeCheck(List<APkgClassResolver> resolvers) throws CompilerException {
        for(APkgClassResolver resolver : resolvers){
            resolver.linkLocalNamesToDcl();
        }
    }

    private static void buildAllResolvers(List<APkgClassResolver> resolvers) throws CompilerException {
        for(APkgClassResolver resolver : resolvers) resolver.build();
    }

    private static void generateCode(List<APkgClassResolver> resolvers, boolean outputFile) throws IOException{
        PrintStream printer;

        SelectorIndexedTable sit = SelectorIndexedTable.generateSIT(resolvers, outputFile, OUTPUT_DIRECTORY);
        SubtypeIndexedTable subIt = SubtypeIndexedTable.generateTable(resolvers, outputFile, OUTPUT_DIRECTORY);

        for (APkgClassResolver resolver : resolvers) {
            resolver.computeFieldOffsets();
        }

        StaticFieldInit.generateCode(resolvers, sit, subIt, outputFile, OUTPUT_DIRECTORY);
        CodeGenVisitor codeGen = new CodeGenVisitor(sit, subIt);
        for(APkgClassResolver resolver : resolvers){
            if(!resolver.shouldGenCode()) continue;
            codeGen.genLayoutForStaticFields(resolver.getUninheritedStaticFields());
            codeGen.genHeader(resolver);
            resolver.generateCode(codeGen);
            if (outputFile){
                File file;
                if(resolver.pkg == APkgClassResolver.DEFAULT_PKG) file = new File(OUTPUT_DIRECTORY + resolver.name + ".s");
                else file = new File(OUTPUT_DIRECTORY + resolver.fullName + ".s");
                file.createNewFile();
                printer = new PrintStream(file);
            }else{
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                printer = new PrintStream(baos);
            }

            codeGen.printToFileAndEmpty(printer);
            printer.close();
        }
    }

    private static void printUsage() {
        System.err.println("Usage: ./joosc file1 [file2 [file3 ...]]");
    }

    private static ANonTerminal parse(Reader reader)
            throws FileNotFoundException, IOException, LexerException,
            UnexpectedTokenException, URISyntaxException {
        ANonTerminal parseTree;

        Lexer lexer = new Lexer(reader);

        Parser parser = new Parser(new TextReadingRules());

        parseTree = parser.parse(lexer);
        return parseTree;
    }
}
