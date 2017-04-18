
public class IfPreprocessorBranch extends ConditionalPreprocessorBranch implements IConditionalPreprocessorFirstBranch {
	private PreprocessorCondition condition;

	public PreprocessorCondition getCondition() {
		return condition;
	}

	public void setCondition(PreprocessorCondition condition) {
		this.condition = condition;
	}

	@Override
	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}

}
