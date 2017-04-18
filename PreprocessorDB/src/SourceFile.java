import java.util.List;

public class SourceFile {

	private List<IPreprocessorDirective> preprocessorDirective;

	public List<IPreprocessorDirective> getPreprocessorDirective() {
		return preprocessorDirective;
	}

	public void setPreprocessorDirective(List<IPreprocessorDirective> preprocessorDirective) {
		this.preprocessorDirective = preprocessorDirective;
	}
}
