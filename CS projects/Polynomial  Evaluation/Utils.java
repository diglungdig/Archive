import java.util.List;


public class Utils {
	private static String getTermString(ExpressionAtom operand) {
		String tokenString = "";
		if(operand.getVariablesOrOperator().isEmpty() || 
				Math.abs(operand.getCoefficient()) != 1) {
			tokenString += String.valueOf(operand.getCoefficient());
		} else if(operand.getCoefficient() == -1)
				tokenString += "-";

		tokenString += operand.getVariablesOrOperator();
		
		return tokenString;
	}

	public static void printExpression(List<ExpressionAtom> expression) {
		String output = "";
		for(ExpressionAtom token: expression) {
			output += (" " + getTermString(token));
		}
		System.out.println(output.trim());
	}

	public static void printListRepresentation(ListRepresentation listExpression) {
		if(listExpression.isNegative())
			System.out.print("-");
		System.out.print("[");
		
		if(listExpression.operands.isEmpty()) {
			System.out.print(getTermString(listExpression.getNodeVal()));
		} else {
			System.out.print(listExpression.getNodeVal().getVariablesOrOperator() + ", ");
			for(int i = 0 ; i < listExpression.operands.size(); ++i) {
				if(i > 0)
					System.out.print(", ");
				
				printListRepresentation(listExpression.operands.get(i));
			}
		}
		
		System.out.print("]");
	}
	
	public static String getCleanedString(String text) {
		return text.replaceAll("\\s", "");
	}
	
	private static String makeVariableCompact(String variable) {
		String compactVar = "";
		
		char[] literals = variable.toCharArray();
		int count = 0;
		char currentChar = '@'; 
		
		for(int i = 0; i < literals.length; ++i) {
			if(literals[i] != currentChar) {
				if(count > 0)
					compactVar += (String.valueOf(currentChar));
				
				if(count > 1)
					compactVar += ("^" + String.valueOf(count));
				
				currentChar = literals[i];
				count = 1;
			} else
				++count;
		}
		
		if(currentChar != '@')
			compactVar += String.valueOf(currentChar);
		if(count > 1)
			compactVar += ("^" + String.valueOf(count));
		
		return compactVar;	
	}
	
	private static String collate(ExpressionAtom token) {
		String output = "";
		
		if(token.getCoefficient() < 0) {
			output += " - ";
		} else if(token.getCoefficient() > 0) {
			output += " + ";
		} else {
			return output;
		}
		
		if(token.getVariablesOrOperator().isEmpty()) {
			output += String.valueOf(Math.abs(token.getCoefficient()));
		} else {
			if(token.getCoefficient() != 1 && token.getCoefficient() != -1)
				output += String.valueOf(Math.abs(token.getCoefficient()));
			output += makeVariableCompact(token.getVariablesOrOperator());
		}
		return output;
	}
	
	public static void printOutputExpression(List<ExpressionAtom> outputString) {
		String output = collate(outputString.get(0));
		for(int i = 1; i < outputString.size(); ++i) {
			output += collate(outputString.get(i));
		}
		
		output = output.trim();
		if(output.startsWith("+")) {
			System.out.println(output.substring(2));
		} else {
			if(output.isEmpty())
				System.out.println("0");
			else
				System.out.println(output);
		}
	}
}