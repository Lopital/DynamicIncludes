
public interface IPreprocessorVisitor {

	Object visit(DefinePreprocessorDirective definePreprocessorDirective);

	Object visit(IncludePreprocessorDirective includePreprocessorDirective);

	Object visit(IfPreprocessorBranch ifPreprocessorBranch);

	Object visit(ConditionalPreprocessorDirective conditionalPreprocessorDirective);

	Object visit(ElifPreprocessorBranch elifPreprocessorBranch);

	Object visit(ElsePreprocessorBranch elsePreprocessorBranch);

	Object visit(IfdefPreprocessorBranch ifdefPreprocessorBranch);

	Object visit(IfndefPreprocessorBranch ifndefPreprocessorBranch);

	Object visit(UndefPreprocessorDirective undefPreprocessorDirective);

	Object visit(SourceFile sourceFile);

	Object visit(PreprocessorCondition preprocessorCondition);

}
