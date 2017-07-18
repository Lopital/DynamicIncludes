package preprocessor.db.directives;

public interface IPreprocessorDirective extends IPreprocessorVisitable {

	PreprocessorLocation getLocation();

	void setLocation(PreprocessorLocation location);

}
