public class ExpressionAtom {
	private int coefficient;
	private String variablesOrOperator;
	private AtomType atomType;
	
	public ExpressionAtom(String inputVariablesOrOperator, AtomType inputAtomType, 
			int inputCoefficient) {
		this.variablesOrOperator = inputVariablesOrOperator;
		this.atomType = inputAtomType;
		this.coefficient = inputCoefficient;
	}
	
	public void setVariablesOrOperator(String inputVariablesOrOperator) {
		this.variablesOrOperator = inputVariablesOrOperator;
	}
	
	public void setAtomType(AtomType inputAtomType) {
		this.atomType = inputAtomType;
	}
	
	public void setCoefficient(int inputCoefficient) {
		this.coefficient = inputCoefficient;
	}
	
	public String getVariablesOrOperator() {
		return this.variablesOrOperator;
	}
	
	public AtomType getAtomType() {
		return this.atomType;
	}
	
	public int getCoefficient() {
		return this.coefficient;
	}
	
	public void addCoefficient(int addCoefficient) {
		this.coefficient += addCoefficient;
	}
}