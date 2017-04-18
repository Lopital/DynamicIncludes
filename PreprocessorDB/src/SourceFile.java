
import java.util.List;

public class SourceFile implements IPreprocessorContainer {

	private List<IPreprocessorDirective> preprocessorDirectives;

	public List<IPreprocessorDirective> getPreprocessorDirectives() {
		return preprocessorDirectives;
	}

	public void setPreprocessorDirectives(List<IPreprocessorDirective> preprocessorDirectives) {
		this.preprocessorDirectives = preprocessorDirectives;
	}

	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
