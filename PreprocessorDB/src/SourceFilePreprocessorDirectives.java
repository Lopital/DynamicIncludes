
public class SourceFilePreprocessorDirectives extends PreprocessorContainer implements IPreprocessorContainer {

	private String guardMacro;

	public String getGuardMacro() {
		return guardMacro;
	}

	public void setGuardMacro(String guardMacro) {
		this.guardMacro = guardMacro;
	}

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
