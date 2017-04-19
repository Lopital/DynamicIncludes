
public class SourceFile extends PreprocessorContainer implements IPreprocessorContainer {

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
