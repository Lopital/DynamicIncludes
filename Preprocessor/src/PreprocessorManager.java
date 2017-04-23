import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;

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
		
	}
	
	private void updatePreprocessors(Project project) {
		for (SourceFile sourceFile : project.getFiles()) {

			SourceFilePreprocessorDirectives filePreprocessorDirectives = null;
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
				sourceFile.setFilePreprocessorDirectives(filePreprocessorDirectives);
			}
		}
	}
}
