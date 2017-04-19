import java.nio.file.Path;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public final class GrammarManager {

	private PreprocessorExtractor extractor = new PreprocessorExtractor();

	public SourceFile extractPreprocessor(Path filePath) {
		CLangPreprocessorLexer lexer = new CLangPreprocessorLexer(new ANTLRInputStream(filePath.toString()));
		TokenStream tokenStream = new CommonTokenStream(lexer);
		CLangPreprocessorParser parser = new CLangPreprocessorParser(tokenStream);
		parser.setBuildParseTree(false);
		extractor.Init(filePath);
		parser.addParseListener(extractor);
		parser.sourceFile();
		SourceFile sourceFile = extractor.getSourceFile();
		return sourceFile;
	}
}
