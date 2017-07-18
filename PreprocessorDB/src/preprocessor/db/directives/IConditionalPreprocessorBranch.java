package preprocessor.db.directives;

public interface IConditionalPreprocessorBranch extends IPreprocessorContainer, IPreprocessorVisitable {

	PreprocessorLocation getStartLocation();

	void setStartLocation(PreprocessorLocation location);

	PreprocessorLocation getEndLocation();

	void setEndLocation(PreprocessorLocation location);

}
