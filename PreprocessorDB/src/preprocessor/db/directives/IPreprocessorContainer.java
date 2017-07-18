package preprocessor.db.directives;

import java.util.List;

public interface IPreprocessorContainer extends IPreprocessorVisitable {

	List<IPreprocessorDirective> getPreprocessorDirectives();

	void setPreprocessorDirectives(List<IPreprocessorDirective> preprocessorDirective);
}
