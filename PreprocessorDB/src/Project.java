
public class Project {

	private Configuration configuration;
	private SourceFileKeyedCollection sourceFiles;
	private SourceFileKeyedCollection headerFiles;

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public SourceFileKeyedCollection getSourceFiles() {
		return sourceFiles;
	}

	public void setSourceFiles(SourceFileKeyedCollection files) {
		this.sourceFiles = files;
	}

	public SourceFileKeyedCollection getHeaderFiles() {
		return headerFiles;
	}

	public void setHeaderFiles(SourceFileKeyedCollection headerFiles) {
		this.headerFiles = headerFiles;
	}
}
