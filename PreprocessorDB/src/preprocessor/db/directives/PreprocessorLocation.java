package preprocessor.db.directives;

public class PreprocessorLocation implements IPreprocessorLocation {

	private int line;

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public void setLine(int line) {
		this.line = line;
	}

}
