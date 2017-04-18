
public class IfndefPreprocessorBranch extends ConditionalPreprocessorBranch
		implements IConditionalPreprocessorFirstBranch {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
