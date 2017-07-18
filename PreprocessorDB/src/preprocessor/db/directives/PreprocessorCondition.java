package preprocessor.db.directives;

public class PreprocessorCondition {

	private String condition;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
