package preprocessor.db.directives;

public abstract class PreprocessorDirective implements IPreprocessorDirective {

	private PreprocessorLocation location;

	@Override
	public PreprocessorLocation getLocation() {
		return location;
	}

	@Override
	public void setLocation(PreprocessorLocation location) {
		this.location = location;
	}

}
