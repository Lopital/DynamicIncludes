import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public final class GrammarManager {
	
	public void runAntlrOnFile(String filePath, PreprocessorExtractor listener) {
		CLangPreprocessorLexer lexer = new CLangPreprocessorLexer(new ANTLRInputStream(filePath));
		TokenStream tokenStream = new CommonTokenStream(lexer);
		CLangPreprocessorParser parser = new CLangPreprocessorParser(tokenStream);
		parser.setBuildParseTree(false);
		parser.addParseListener(listener);
		parser.sourceFile();
	}
}
