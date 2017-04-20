import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public final class GrammarManager {

	private PreprocessorExtractor extractor = new PreprocessorExtractor();

	public SourceFile extractPreprocessor(Path filePath)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		CLangPreprocessorLexer lexer = new CLangPreprocessorLexer(
				new ANTLRInputStream(new InputStreamReader(new FileInputStream(filePath.toString()), "Cp1252")));
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
