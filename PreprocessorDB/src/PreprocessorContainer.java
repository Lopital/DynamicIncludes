import java.util.ArrayList;
import java.util.List;

public abstract class PreprocessorContainer implements IPreprocessorContainer {

	private List<IPreprocessorDirective> preprocessorDirectives = new ArrayList<IPreprocessorDirective>();

	@Override
	public List<IPreprocessorDirective> getPreprocessorDirectives() {
		return preprocessorDirectives;
	}

	@Override
	public void setPreprocessorDirectives(List<IPreprocessorDirective> preprocessorDirectives) {
		this.preprocessorDirectives = preprocessorDirectives;
	}

}
