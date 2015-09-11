import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class EvaluatePolynomials {
		
	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		
		int nCases = in.nextInt();
		in.nextLine();
		
		for(int i = 0; i < nCases; ++i) {
			String input = in.nextLine();
			
			if(i > 0) {
				System.out.println("");
			}
			System.out.println("Test Case " + (i + 1) + ": " + Utils.getCleanedString(input));

			Polynomial inputPolynomial = new Polynomial(Utils.getCleanedString(input));
			
			System.out.print("\t");
			Utils.printListRepresentation(inputPolynomial.listRepresentation);
			System.out.println("");
			
			inputPolynomial.evaluate();
			
			

			Collections.sort(inputPolynomial.finalExpression, new Comparator<ExpressionAtom>(){
				private int getDegree(String operand) {
					try {
						Integer.valueOf(operand);
						return 0;
					} catch(Exception e) {
						return operand.length();
					}
				}

				public int compare(ExpressionAtom o1, ExpressionAtom o2){
			    	String var1 = o1.getVariablesOrOperator();
			    	String var2 = o2.getVariablesOrOperator();
			    	int degree1 = getDegree(var1);
			    	int degree2 = getDegree(var2);
			    	
			    	if(degree1 < degree2 || 
			    			(degree1 == degree2 && (var1.compareTo(var2) > 0)))
			    		return 1;
			    	else if(degree1 == degree2 && var1.equals(var2))
			    		return 0;
			    	return -1;
			    }
			});

			System.out.print("\t");
			Utils.printOutputExpression(inputPolynomial.finalExpression);	
		}
		
		in.close();
	}
}
