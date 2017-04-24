import java.nio.file.Path;

public class SourceFile {

	private Configuration configuration;
	private SourceFilePreprocessorDirectives filePreprocessorDirectives;
	private String guardMacro;
	private Path filePath;

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public SourceFilePreprocessorDirectives getFilePreprocessorDirectives() {
		return filePreprocessorDirectives;
	}

	public void setFilePreprocessorDirectives(SourceFilePreprocessorDirectives filePreprocessorDirectives) {
		this.filePreprocessorDirectives = filePreprocessorDirectives;
	}

	public String getGuardMacro() {
		return guardMacro;
	}

	public void setGuardMacro(String guardMacro) {
		this.guardMacro = guardMacro;
	}

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

}
