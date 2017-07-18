package preprocessor.db.directives;

public interface IPreprocessorVisitable {

	Object accept(IPreprocessorVisitor visitor);

}
