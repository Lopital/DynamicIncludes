
public class ElifPreprocessorBranch extends ConditionalPreprocessorBranch
		implements IConditionalPreprocessorSecondBranch {
	private PreprocessorCondition condition;

	public PreprocessorCondition getCondition() {
		return condition;
	}

	public void setCondition(PreprocessorCondition condition) {
		this.condition = condition;
	}
}
