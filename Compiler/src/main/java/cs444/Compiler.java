package cs444;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.StaticFieldInit;
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

import java.io.*;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Compiler {
    //NOTE, if I want to allow the output directory to change, I need to copy each OS's runtime for each platform.
    public static final String BASE_DIRECTORY = "/Volumes/RAM/";
    //default to all supported platforms;
    public static final String[] defaultPlatforms = {"-x86", "-x64", "-a32"};

    public static final String OUTPUT_DIRECTORY = BASE_DIRECTORY + "output";
    public static final int COMPILER_ERROR_CODE = 42;

    /**
     * @param args
     * @throws URISyntaxException
     * @throws Exception
     */
    public static void main(String[] args) {
        CompilerSettings cs = new CompilerSettings(args);
        System.exit(compile(cs.files, true, true, cs.platforms));
    }

    public static int compile(List<String> files, boolean printErrors, boolean outputFiles,
                              Collection<Platform<?, ?>> platforms) {

        if (files.size() == 0) {
            System.err.println("ERROR: At least a file should be passed.");
            printUsage();
            return COMPILER_ERROR_CODE;
        }

        Reader reader = null;
        ANonTerminal parseTree = null;

        try {
            for (String fileName : files) {
                reader = new FileReader(fileName);
                parseTree = parse(reader);

                IASTBuilder builder = new JoosASTBuilder(new File(fileName).getName());
                parseTree = (ANonTerminal) builder.build(parseTree);

                PkgClassInfo.instance.addClassOrInterface((AInterfaceOrClassSymbol) parseTree);
            }

            //Make a copy, the symbols can add more for arrays.
            List<APkgClassResolver> resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());
            buildAllResolvers(resolvers);
            //New ones may have been added
            resolvers = new LinkedList<APkgClassResolver>(PkgClassInfo.instance.getSymbols());

            analyzeReachability(resolvers);
            for (APkgClassResolver resolver : resolvers) {
                resolver.linkLocalNamesToDcl(platforms);
            }
            for (APkgClassResolver resolver : resolvers) {
                resolver.checkFields(platforms);
            }
            for (APkgClassResolver resolver : resolvers) {
                resolver.clean();
            }

            for (Platform<?, ?> platform : platforms) {
                generateCode(resolvers, outputFiles, platform);
            }
        } catch (Exception e) {
            if (printErrors) {
                e.printStackTrace();
            }
            return COMPILER_ERROR_CODE;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (printErrors) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return 0;
    }

    private static void analyzeReachability(List<APkgClassResolver> resolvers) throws CompilerException {

        for (APkgClassResolver resolver : resolvers) {
            resolver.reduceToConstantExprs();
            resolver.analyzeReachability();
        }
    }

    private static void buildAllResolvers(List<APkgClassResolver> resolvers) throws CompilerException {
        for (APkgClassResolver resolver : resolvers) {
            resolver.build();
        }
    }

    private static void generateCode(List<APkgClassResolver> resolvers, boolean outputFile,
                                     Platform<?, ?> platform)
            throws IOException {

        PrintStream printer;
        String outputDir = platform.getOutputDir();

        platform.getSelectorIndex().generateSIT(resolvers, outputFile);
        platform.makeSubtypeTable(resolvers, outputFile, outputDir);

        for (APkgClassResolver resolver : resolvers) {
            resolver.computeFieldOffsets(platform);
        }

        StaticFieldInit.generateCode(resolvers, platform, outputFile, outputDir);

        CodeGenVisitor<?, ?> codeGen = platform.makeNewCodeGen();
        for (APkgClassResolver resolver : resolvers) {
            if (!resolver.shouldGenCode()) {
                continue;
            }
            codeGen.genLayoutForStaticFields(resolver.getUninheritedStaticFields());
            codeGen.genHeader(resolver);
            resolver.generateCode(codeGen);
            if (outputFile) {
                File file;
                if (resolver.pkg == APkgClassResolver.DEFAULT_PKG) {
                    file = new File(outputDir, resolver.name + ".s");
                } else {
                    file = new File(outputDir, resolver.fullName + ".s");
                }
                file.createNewFile();
                printer = new PrintStream(file);
            } else {
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

    private static ANonTerminal parse(Reader reader) throws IOException, LexerException,
            UnexpectedTokenException, URISyntaxException {
        ANonTerminal parseTree;

        Lexer lexer = new Lexer(reader);

        Parser parser = new Parser(new TextReadingRules());

        parseTree = parser.parse(lexer);
        return parseTree;
    }
}
