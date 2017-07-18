package preprocessor.db.directives;

public interface IPreprocessorVisitor {

	Object visit(SourceFilePreprocessorDirectives sourceFile);

	Object visit(IncludePreprocessorDirective includePreprocessorDirective);
	
	Object visit(DefinePreprocessorDirective definePreprocessorDirective);

	Object visit(UndefPreprocessorDirective undefPreprocessorDirective);

	Object visit(ConditionalPreprocessorDirective conditionalPreprocessorDirective);
	
	Object visit(PreprocessorCondition preprocessorCondition);

	Object visit(IfPreprocessorBranch ifPreprocessorBranch);

	Object visit(IfdefPreprocessorBranch ifdefPreprocessorBranch);

	Object visit(IfndefPreprocessorBranch ifndefPreprocessorBranch);	

	Object visit(ElifPreprocessorBranch elifPreprocessorBranch);

	Object visit(ElsePreprocessorBranch elsePreprocessorBranch);

}
