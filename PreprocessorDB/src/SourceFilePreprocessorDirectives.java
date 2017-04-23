import java.nio.file.Path;

public class SourceFile extends PreprocessorContainer implements IPreprocessorContainer {

	private Path filePath;

	public Path getFilePath() {
		return filePath;
	}

	public void setFilePath(Path filePath) {
		this.filePath = filePath;
	}

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
