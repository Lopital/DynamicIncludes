
import java.util.ArrayList;
import java.util.List;

public class ConditionalPreprocessorDirective extends PreprocessorDirective {

	private IConditionalPreprocessorBranch ifBranch;
	private List<IConditionalPreprocessorSecondBranch> branchs;

	public IConditionalPreprocessorBranch getIfBranch() {
		return ifBranch;
	}

	public void setIfBranch(IConditionalPreprocessorBranch ifBranch) {
		this.ifBranch = ifBranch;
	}

	public List<IConditionalPreprocessorSecondBranch> getBranchs() {
		return branchs;
	}

	public void setBranchs(List<IConditionalPreprocessorSecondBranch> branchs) {
		this.branchs = branchs;
	}

	public ConditionalPreprocessorDirective(){
		this.branchs = new ArrayList<>();
	}
	
	public Object accept(IPreprocessorVisitor visitor) {
		return visitor.visit(this);
	}
}
