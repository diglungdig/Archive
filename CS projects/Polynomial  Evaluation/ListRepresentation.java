import java.util.ArrayList;
import java.util.List;

public class ListRepresentation {
	private ExpressionAtom nodeVal = null;
	public List<ListRepresentation> operands = new ArrayList<ListRepresentation>();
	private boolean isNegative = false;
	
	public void setNodeVal(ExpressionAtom inputNodeVal) {
		this.nodeVal = inputNodeVal;
	}
	
	public void setNegative(boolean inputIsNegative) {
		this.isNegative = inputIsNegative;
	}
		
	public ExpressionAtom getNodeVal() {
		return this.nodeVal;
	}
	
	public boolean isNegative() {
		return this.isNegative;
	}
}