
public class IncludePreprocessorDirective implements IPreprocessorDirective {

	private String filePath;
	private IncludePathKind kind;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public IncludePathKind getKind() {
		return kind;
	}

	public void setKind(IncludePathKind kind) {
		this.kind = kind;
	}

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
