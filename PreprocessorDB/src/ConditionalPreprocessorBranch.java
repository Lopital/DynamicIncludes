
import java.util.List;

public abstract class ConditionalPreprocessorBranch implements IConditionalPreprocessorBranch, IPreprocessorContainer {
	private List<IPreprocessorDirective> preprocessorDirectives;

	public List<IPreprocessorDirective> getPreprocessorDirectives() {
		return preprocessorDirectives;
	}

	public void setPreprocessorDirectives(List<IPreprocessorDirective> preprocessorDirectives) {
		this.preprocessorDirectives = preprocessorDirectives;
	}

	public abstract Object accept(IPreprocessorVisitor visitor);
}
