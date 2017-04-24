import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//business logic interface
public final class PreprocessorManager {

	private final GrammarManager grammarManager;
	private Charset charset;

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		if (charset == null) {
			throw new NullPointerException();
		}
		this.charset = charset;
	}

	public PreprocessorManager() {
		grammarManager = new GrammarManager();
	}

	public void analyzeProject(Project project) {
		// foreach file in project
		// SourceFileAnalyzerManager
	}

	private void updatePreprocessors(Project project) {
		for (SourceFile sourceFile : project.getFiles()) {

			SourceFilePreprocessorDirectives filePreprocessorDirectives = null;
			String guardMacro = null;
			Path filePath = sourceFile.getFilePath();
			try {
				filePreprocessorDirectives = grammarManager.extractPreprocessor(filePath, charset);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

							if (guardMacro != null) {
								List<IPreprocessorDirective> ifPreprocessorDirectives = firstBranch
										.getPreprocessorDirectives();
								for (IPreprocessorDirective iPreprocessorDirective : ifPreprocessorDirectives) {
									if (iPreprocessorDirective instanceof DefinePreprocessorDirective) {
										DefinePreprocessorDirective define = (DefinePreprocessorDirective) iPreprocessorDirective;
										if (guardMacro.equals(define.getName())) {
											break;
										}

									}
								}
							}
						}
					}
				}
			}

			sourceFile.setFilePreprocessorDirectives(filePreprocessorDirectives);
			sourceFile.setGuardMacro(guardMacro);
		}
	}
}
