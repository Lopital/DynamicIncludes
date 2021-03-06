package test.main;
import java.util.List;

import preprocessor.db.directives.ConditionalPreprocessorDirective;
import preprocessor.db.directives.DefinePreprocessorDirective;
import preprocessor.db.directives.ElifPreprocessorBranch;
import preprocessor.db.directives.ElsePreprocessorBranch;
import preprocessor.db.directives.IConditionalPreprocessorBranch;
import preprocessor.db.directives.IPreprocessorDirective;
import preprocessor.db.directives.IPreprocessorVisitor;
import preprocessor.db.directives.IfPreprocessorBranch;
import preprocessor.db.directives.IfdefPreprocessorBranch;
import preprocessor.db.directives.IfndefPreprocessorBranch;
import preprocessor.db.directives.IncludePreprocessorDirective;
import preprocessor.db.directives.PreprocessorCondition;
import preprocessor.db.directives.SourceFilePreprocessorDirectives;
import preprocessor.db.directives.UndefPreprocessorDirective;

public class PreprocessorDBWriterTest implements IPreprocessorVisitor {

	private StringBuilder sb;

	public StringBuilder getSb() {
		return sb;
	}

	public void init() {
		sb = new StringBuilder();
	}

	@Override
	public Object visit(SourceFilePreprocessorDirectives sourceFile) {
		init();
		appendPreprocessorList(sourceFile.getPreprocessorDirectives());
		return null;
	}

	private void appendPreprocessorList(List<IPreprocessorDirective> list) {
		for (IPreprocessorDirective prepDirective : list) {
			prepDirective.accept(this);
		}
	}

	@Override
	public Object visit(IncludePreprocessorDirective includePreprocessorDirective) {
		sb.append("#include ");
		switch (includePreprocessorDirective.getKind()) {
		case LOCAL_INCLUDE:
			sb.append("\"" + includePreprocessorDirective.getFilePath() + "\"");
			break;
		case SYSTEM_INCLUDE:
			sb.append("<" + includePreprocessorDirective.getFilePath() + ">");
			break;
		case MACRO:
			sb.append(includePreprocessorDirective.getMacro());
			break;
		default:
			sb.append("UNSET include at line " + String.valueOf(includePreprocessorDirective.getLocation().getLine()));
			break;
		}
		sb.append("\r\n");
		return null;
	}

	@Override
	public Object visit(DefinePreprocessorDirective definePreprocessorDirective) {
		sb.append("#define " + definePreprocessorDirective.getName());
		if (definePreprocessorDirective.getParameters() != null) {
			sb.append("(" + String.join(", ", definePreprocessorDirective.getParameters()) + ")");
		}
		sb.append(" ");
		if (definePreprocessorDirective.getValue() != null) {
			sb.append(definePreprocessorDirective.getValue());
		}
		sb.append("\r\n");
		return null;
	}

	@Override
	public Object visit(UndefPreprocessorDirective undefPreprocessorDirective) {
		sb.append("#undef " + undefPreprocessorDirective.getName());
		sb.append("\r\n");
		return null;
	}

	@Override
	public Object visit(ConditionalPreprocessorDirective conditionalPreprocessorDirective) {
		conditionalPreprocessorDirective.getIfBranch().accept(this);
		for (IConditionalPreprocessorBranch branch : conditionalPreprocessorDirective.getBranchs()) {
			branch.accept(this);
		}
		sb.append("#endif");
		sb.append("\r\n");
		return null;
	}

	@Override
	public Object visit(PreprocessorCondition preprocessorCondition) {
		sb.append(preprocessorCondition.getCondition());
		return null;
	}

	@Override
	public Object visit(IfPreprocessorBranch ifPreprocessorBranch) {
		sb.append("#if ");
		ifPreprocessorBranch.getCondition().accept(this);
		sb.append("\r\n");
		appendPreprocessorList(ifPreprocessorBranch.getPreprocessorDirectives());
		return null;
	}

	@Override
	public Object visit(IfdefPreprocessorBranch ifdefPreprocessorBranch) {
		sb.append("#ifdef " + ifdefPreprocessorBranch.getName());
		sb.append("\r\n");
		appendPreprocessorList(ifdefPreprocessorBranch.getPreprocessorDirectives());
		return null;
	}

	@Override
	public Object visit(IfndefPreprocessorBranch ifndefPreprocessorBranch) {
		sb.append("#ifndef " + ifndefPreprocessorBranch.getName());
		sb.append("\r\n");
		appendPreprocessorList(ifndefPreprocessorBranch.getPreprocessorDirectives());
		return null;
	}

	@Override
	public Object visit(ElifPreprocessorBranch elifPreprocessorBranch) {
		sb.append("#elif ");
		elifPreprocessorBranch.getCondition().accept(this);
		sb.append("\r\n");
		appendPreprocessorList(elifPreprocessorBranch.getPreprocessorDirectives());
		return null;
	}

	@Override
	public Object visit(ElsePreprocessorBranch elsePreprocessorBranch) {
		sb.append("#else");
		sb.append("\r\n");
		appendPreprocessorList(elsePreprocessorBranch.getPreprocessorDirectives());
		return null;
	}

}
