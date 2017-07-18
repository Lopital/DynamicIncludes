package preprocessor.db.directives;

public class ElsePreprocessorBranch extends ConditionalPreprocessorBranch
		implements IConditionalPreprocessorSecondBranch {

	@Override
	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
