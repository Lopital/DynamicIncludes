
public abstract class ConditionalPreprocessorBranch extends PreprocessorContainer
		implements IConditionalPreprocessorBranch {
	private PreprocessorLocation startLocation;
	private PreprocessorLocation endLocation;

	@Override
	public PreprocessorLocation getStartLocation() {
		return startLocation;
	}

	@Override
	public void setStartLocation(PreprocessorLocation location) {
		startLocation = location;
	}

	@Override
	public PreprocessorLocation getEndLocation() {
		return endLocation;
	}

	@Override
	public void setEndLocation(PreprocessorLocation location) {
		endLocation = location;
	}

	public abstract Object accept(IPreprocessorVisitor visitor);

}
