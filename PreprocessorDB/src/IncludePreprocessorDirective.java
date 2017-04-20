
public class IncludePreprocessorDirective extends PreprocessorDirective {

	private String filePath;
	private String macro;
	private IncludePathKind kind;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMacro() {
		return macro;
	}

	public void setMacro(String macro) {
		this.macro = macro;
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
