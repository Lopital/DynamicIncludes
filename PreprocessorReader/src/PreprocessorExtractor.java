import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Stack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PreprocessorExtractor extends CLangPreprocessorParserBaseListener {

	private Stack<IPreprocessorContainer> preprocessorContainers;

	private SourceFile sourceFile;

	public SourceFile getSourceFile() {
		return sourceFile;
	}

	public void Init(Path filePath) {
		preprocessorContainers = new Stack<IPreprocessorContainer>();
		sourceFile = new SourceFile();
		sourceFile.setFilePath(filePath);
	}

	@Override
	public void enterSourceFile(CLangPreprocessorParser.SourceFileContext ctx) {
		preprocessorContainers.push(sourceFile);
	}

	@Override
	public void exitSourceFile(CLangPreprocessorParser.SourceFileContext ctx) {
		sourceFile = (SourceFile) preprocessorContainers.pop();
		preprocessorContainers = null;
	}

	@Override
	public void enterPreprocessorDirective(CLangPreprocessorParser.PreprocessorDirectiveContext ctx) {
		// do nothing
	}

	@Override
	public void exitPreprocessorDirective(CLangPreprocessorParser.PreprocessorDirectiveContext ctx) {
		// do nothing
	}

	@Override
	public void enterIncludePrep(CLangPreprocessorParser.IncludePrepContext ctx) {
	}

	@Override
	public void exitIncludePrep(CLangPreprocessorParser.IncludePrepContext ctx) {
		IncludePreprocessorDirective includePreprocessorDirective = new IncludePreprocessorDirective();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		includePreprocessorDirective.setLocation(location);

		if (ctx.localFilePath != null) {
			includePreprocessorDirective.setFilePath(ctx.localFilePath.getText());
			includePreprocessorDirective.setKind(IncludePathKind.LOCAL_INCLUDE);
		} else if (ctx.systemFilePath != null) {
			includePreprocessorDirective.setFilePath(ctx.systemFilePath.getText());
			includePreprocessorDirective.setKind(IncludePathKind.SYSTEM_INCLUDE);
		} else if (ctx.macro != null) {
			includePreprocessorDirective.setMacro(ctx.macro.getText());
			includePreprocessorDirective.setKind(IncludePathKind.MACRO);
		} else {
			// TODO: save error.
			includePreprocessorDirective.setFilePath(null);
			includePreprocessorDirective.setKind(IncludePathKind.NOT_SET);
		}
		preprocessorContainers.peek().getPreprocessorDirectives().add(includePreprocessorDirective);
	}

	@Override
	public void enterDefinePrep(CLangPreprocessorParser.DefinePrepContext ctx) {
	}

	@Override
	public void exitDefinePrep(CLangPreprocessorParser.DefinePrepContext ctx) {
		DefinePreprocessorDirective definePreprocessorDirective = new DefinePreprocessorDirective();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		definePreprocessorDirective.setLocation(location);

		definePreprocessorDirective.setName(ctx.macro.getText());

		if (ctx.param != null) {
			definePreprocessorDirective.setParameters(new ArrayList<String>());
			if (ctx.param.names != null) {
				for (Token param : ctx.param.names) {
					definePreprocessorDirective.getParameters().add(param.getText());
				}
			}
		}

		String value = null;
		if (ctx.valueParts != null && ctx.valueParts.size() > 0) {
			value = "";
			for (Token token : ctx.valueParts) {
				value += " " + token.getText();
			}
		}
		definePreprocessorDirective.setValue(value);
		preprocessorContainers.peek().getPreprocessorDirectives().add(definePreprocessorDirective);
	}

	@Override
	public void enterParameters(CLangPreprocessorParser.ParametersContext ctx) {
		// do nothing
	}

	@Override
	public void exitParameters(CLangPreprocessorParser.ParametersContext ctx) {
		// do nothing
	}

	@Override
	public void enterUndefPrep(CLangPreprocessorParser.UndefPrepContext ctx) {
	}

	@Override
	public void exitUndefPrep(CLangPreprocessorParser.UndefPrepContext ctx) {
		UndefPreprocessorDirective undefPreprocessorDirective = new UndefPreprocessorDirective();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		undefPreprocessorDirective.setLocation(location);

		undefPreprocessorDirective.setName(ctx.macro.getText());
		preprocessorContainers.peek().getPreprocessorDirectives().add(undefPreprocessorDirective);
	}

	@Override
	public void enterIfPrep(CLangPreprocessorParser.IfPrepContext ctx) {
	}

	@Override
	public void exitIfPrep(CLangPreprocessorParser.IfPrepContext ctx) {
		ConditionalPreprocessorDirective conditionalPreprocessorDirective = new ConditionalPreprocessorDirective();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		conditionalPreprocessorDirective.setLocation(location);

		IfPreprocessorBranch ifPreprocessorBranch = new IfPreprocessorBranch();
		ifPreprocessorBranch.setStartLocation(location);

		PreprocessorCondition preprocessorCondition = new PreprocessorCondition();
		String condition = "";
		for (Token part : ctx.conditionParts) {
			condition += " " + part.getText();
		}
		preprocessorCondition.setCondition(condition);
		ifPreprocessorBranch.setCondition(preprocessorCondition);

		conditionalPreprocessorDirective.setIfBranch(ifPreprocessorBranch);
		preprocessorContainers.peek().getPreprocessorDirectives().add(conditionalPreprocessorDirective);
		preprocessorContainers.push(ifPreprocessorBranch);
	}

	@Override
	public void enterIfdefPrep(CLangPreprocessorParser.IfdefPrepContext ctx) {
	}

	@Override
	public void exitIfdefPrep(CLangPreprocessorParser.IfdefPrepContext ctx) {
		ConditionalPreprocessorDirective conditionalPreprocessorDirective = new ConditionalPreprocessorDirective();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		conditionalPreprocessorDirective.setLocation(location);

		IfdefPreprocessorBranch ifdefPreprocessorBranch = new IfdefPreprocessorBranch();
		ifdefPreprocessorBranch.setStartLocation(location);
		ifdefPreprocessorBranch.setName(ctx.macro.getText());

		conditionalPreprocessorDirective.setIfBranch(ifdefPreprocessorBranch);

		preprocessorContainers.peek().getPreprocessorDirectives().add(conditionalPreprocessorDirective);
		preprocessorContainers.push(ifdefPreprocessorBranch);
	}

	@Override
	public void enterIfndefPrep(CLangPreprocessorParser.IfndefPrepContext ctx) {
	}

	@Override
	public void exitIfndefPrep(CLangPreprocessorParser.IfndefPrepContext ctx) {
		ConditionalPreprocessorDirective conditionalPreprocessorDirective = new ConditionalPreprocessorDirective();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		conditionalPreprocessorDirective.setLocation(location);

		IfndefPreprocessorBranch ifndefPreprocessorBranch = new IfndefPreprocessorBranch();
		ifndefPreprocessorBranch.setStartLocation(location);
		ifndefPreprocessorBranch.setName(ctx.macro.getText());
		conditionalPreprocessorDirective.setIfBranch(ifndefPreprocessorBranch);

		preprocessorContainers.peek().getPreprocessorDirectives().add(conditionalPreprocessorDirective);
		preprocessorContainers.push(ifndefPreprocessorBranch);
	}

	@Override
	public void enterElifPrep(CLangPreprocessorParser.ElifPrepContext ctx) {
		IConditionalPreprocessorBranch branch = (IConditionalPreprocessorBranch) preprocessorContainers.pop();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());

		branch.setEndLocation(location);
	}

	@Override
	public void exitElifPrep(CLangPreprocessorParser.ElifPrepContext ctx) {
		ConditionalPreprocessorDirective conditionalPreprocessorDirective = (ConditionalPreprocessorDirective) preprocessorContainers
				.peek().getPreprocessorDirectives()
				.get(preprocessorContainers.peek().getPreprocessorDirectives().size() - 1);

		ElifPreprocessorBranch elifPreprocessorBranch = new ElifPreprocessorBranch();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		elifPreprocessorBranch.setStartLocation(location);

		PreprocessorCondition preprocessorCondition = new PreprocessorCondition();
		String condition = "";
		for (Token part : ctx.conditionParts) {
			condition += " " + part.getText();
		}
		preprocessorCondition.setCondition(condition);
		elifPreprocessorBranch.setCondition(preprocessorCondition);

		conditionalPreprocessorDirective.getBranchs().add(elifPreprocessorBranch);
		preprocessorContainers.push(elifPreprocessorBranch);
	}

	@Override
	public void enterElsePrep(CLangPreprocessorParser.ElsePrepContext ctx) {
		IConditionalPreprocessorBranch branch = (IConditionalPreprocessorBranch) preprocessorContainers.pop();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());

		branch.setEndLocation(location);
	}

	@Override
	public void exitElsePrep(CLangPreprocessorParser.ElsePrepContext ctx) {
		ConditionalPreprocessorDirective conditionalPreprocessorDirective = (ConditionalPreprocessorDirective) preprocessorContainers
				.peek().getPreprocessorDirectives()
				.get(preprocessorContainers.peek().getPreprocessorDirectives().size() - 1);

		ElsePreprocessorBranch elsePreprocessorBranch = new ElsePreprocessorBranch();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());
		elsePreprocessorBranch.setStartLocation(location);

		conditionalPreprocessorDirective.getBranchs().add(elsePreprocessorBranch);
		preprocessorContainers.push(elsePreprocessorBranch);
	}

	@Override
	public void enterEndifPrep(CLangPreprocessorParser.EndifPrepContext ctx) {
		IConditionalPreprocessorBranch branch = (IConditionalPreprocessorBranch) preprocessorContainers.pop();

		PreprocessorLocation location = new PreprocessorLocation();
		location.setLine(ctx.start.getLine());

		branch.setEndLocation(location);
	}

	@Override
	public void exitEndifPrep(CLangPreprocessorParser.EndifPrepContext ctx) {
	}

	@Override
	public void enterEveryRule(ParserRuleContext ctx) {
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
	}

	@Override
	public void visitTerminal(TerminalNode node) {
	}

	@Override
	public void visitErrorNode(ErrorNode node) {
		// TODO: save error!
	}

}
