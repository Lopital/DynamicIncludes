import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public final class GrammarManager {

	private PreprocessorExtractor extractor = new PreprocessorExtractor();

	public SourceFilePreprocessorDirectives extractPreprocessor(Path filePath, Charset charset)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		CLangPreprocessorLexer lexer = new CLangPreprocessorLexer(
				new ANTLRInputStream(new InputStreamReader(new FileInputStream(filePath.toString()), charset)));
		TokenStream tokenStream = new CommonTokenStream(lexer);
		CLangPreprocessorParser parser = new CLangPreprocessorParser(tokenStream);
		parser.setBuildParseTree(false);
		extractor.Init();
		parser.addParseListener(extractor);
		parser.sourceFile();
		SourceFilePreprocessorDirectives sourceFile = extractor.getSourceFile();
		String guardMacro = getGuardMacro(sourceFile);
		sourceFile.setGuardMacro(guardMacro);
		List<String> dependencies = getDependencies(sourceFile);
		return sourceFile;
	}

	private List<String> getDependencies(SourceFilePreprocessorDirectives sourceFile) {
		//TODO extract dependencies!
		List<String> deps = new ArrayList<>();
		
		/* visitor that traverse all conditional directives
		 * parser for #if condition
		 * data structure for condition
		 */
		
		return deps;
	}

	private String getGuardMacro(SourceFilePreprocessorDirectives filePreprocessorDirectives) {
		String guardMacro = null;
		if (filePreprocessorDirectives != null) {
			List<IPreprocessorDirective> preprocessorDirectives = filePreprocessorDirectives
					.getPreprocessorDirectives();
			if (preprocessorDirectives.size() == 1) {
				IPreprocessorDirective preprocessorDirective = preprocessorDirectives.get(0);
				if (preprocessorDirective instanceof ConditionalPreprocessorDirective) {
					ConditionalPreprocessorDirective conditionalPreprocessorDirective = (ConditionalPreprocessorDirective) preprocessorDirective;
					if (conditionalPreprocessorDirective.getBranchs().size() == 0) {
						IConditionalPreprocessorFirstBranch firstBranch = conditionalPreprocessorDirective
								.getIfBranch();

						if (firstBranch instanceof IfndefPreprocessorBranch) {
							IfndefPreprocessorBranch ifndefPreprocessorBranch = (IfndefPreprocessorBranch) firstBranch;
							guardMacro = ifndefPreprocessorBranch.getName();
						} else if (firstBranch instanceof IfPreprocessorBranch) {
							IfPreprocessorBranch ifPreprocessorBranch = (IfPreprocessorBranch) firstBranch;
							PreprocessorCondition preprocessorCondition = ifPreprocessorBranch.getCondition();
							String condition = preprocessorCondition.getCondition();
							String noParenCondition = condition.replaceAll("[\\(\\)]", " ");
							Pattern pattern = Pattern.compile("^\\s*!\\s*defined\\s+(\\w+)\\s*$");
							Matcher matcher = pattern.matcher(noParenCondition);
							if (matcher.matches()) {
								guardMacro = matcher.group(1);
							}
						}

						boolean isGuardDefined = false;
						if (guardMacro != null) {
							List<IPreprocessorDirective> ifPreprocessorDirectives = firstBranch
									.getPreprocessorDirectives();
							for (IPreprocessorDirective iPreprocessorDirective : ifPreprocessorDirectives) {
								if (iPreprocessorDirective instanceof DefinePreprocessorDirective) {
									DefinePreprocessorDirective define = (DefinePreprocessorDirective) iPreprocessorDirective;
									if (guardMacro.equals(define.getName())) {
										isGuardDefined = true;
										break;
									}

								}
							}
						}

						if (!isGuardDefined) {
							guardMacro = null;
						}
					}
				}
			}
		}
		return guardMacro;
	}
}
