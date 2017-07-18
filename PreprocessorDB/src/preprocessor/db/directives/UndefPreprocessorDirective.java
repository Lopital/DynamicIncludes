package preprocessor.db.directives;

public class UndefPreprocessorDirective extends PreprocessorDirective {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
