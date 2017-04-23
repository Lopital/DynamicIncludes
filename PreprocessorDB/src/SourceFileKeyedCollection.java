import java.nio.file.Path;

public class SourceFileKeyedCollection extends KeyedCollection<Path, SourceFile> {

	@Override
	protected Path getKey(SourceFile value) {
		return value.getFilePath();
	}

}
