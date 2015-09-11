import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class Polynomial {
	List<ExpressionAtom> infixExpression = new ArrayList<ExpressionAtom>();

	List<ExpressionAtom> finalExpression;

	ListRepresentation listRepresentation;

	private String identifyUnaryMinuses(String expression) {
		if (expression.startsWith("-")) {
			expression = "%" + expression.substring(1);
		}

		char[] expressionAtoms = expression.toCharArray();
		String returnExpression = String.valueOf(expressionAtoms[0]);
		for (int i = 1; i < expressionAtoms.length; ++i) {
			if (expressionAtoms[i] == '-' && expressionAtoms[i - 1] == '(')
				returnExpression += "%";
			else
				returnExpression += String.valueOf(expressionAtoms[i]);
		}

		return returnExpression;
	}

	private String insertMultiplicationSigns(String expression) {
		char[] expressionAtoms = expression.toCharArray();
		String returnExpression = String.valueOf(expressionAtoms[0]);

		for (int i = 1; i < expressionAtoms.length; ++i) {
			if (!isOperator(expressionAtoms[i])
					&& !Character.isDigit(expressionAtoms[i])
					&& Character.isDigit(expressionAtoms[i - 1]))
				returnExpression += "*";
			returnExpression += String.valueOf(expressionAtoms[i]);
		}

		return returnExpression;
	}

	private boolean isOperator(char token) {
		return token == '+' || token == '-' || token == '*' || token == '^'
				|| token == '(' || token == ')';
	}

	private List<ExpressionAtom> parseInputPolynomial(String inputExpression) {
		inputExpression = identifyUnaryMinuses(inputExpression);
		inputExpression = insertMultiplicationSigns(inputExpression);

		List<ExpressionAtom> inputExpressionTokens = new ArrayList<ExpressionAtom>();

		char[] inputChars = inputExpression.toCharArray();
		for (int i = 0; i < inputChars.length; ++i) {
			if (isOperator(inputChars[i]) || inputChars[i] == '%') {
				// each expressionAtom is separated by a Operator type
				// expressionAtom
				inputExpressionTokens.add(new ExpressionAtom(String
						.valueOf(inputChars[i]), AtomType.OPERATOR, 1));
			} else {
				int lastIndex = inputExpressionTokens.size() - 1;
				if (lastIndex >= 0
						&& inputExpressionTokens.get(lastIndex).getAtomType() == AtomType.OPERAND) {
					// cases where there is a pre-existing expressionAtom at the
					// end
					ExpressionAtom lastElement = inputExpressionTokens
							.remove(lastIndex);
					if (Character.isDigit(inputChars[i])) {
						lastElement
								.setCoefficient(lastElement.getCoefficient()
										* 10
										+ Character
												.getNumericValue(inputChars[i]));
					} else {
						lastElement.setVariablesOrOperator(lastElement
								.getVariablesOrOperator()
								+ String.valueOf(inputChars[i]));
					}
					inputExpressionTokens.add(lastElement);
				} else if (Character.isDigit(inputChars[i])) {
					// cases where there is a operator at the end

					// add new expressionAtom of a single number at the end of
					// the list
					inputExpressionTokens.add(new ExpressionAtom("",
							AtomType.OPERAND, Character
									.getNumericValue(inputChars[i])));
				} else {
					// add new expressionAtom of a single letter at the end of
					// the list
					inputExpressionTokens.add(new ExpressionAtom(String
							.valueOf(inputChars[i]), AtomType.OPERAND, 1));
				}
			}
		}

		return inputExpressionTokens;
	}

	private ListRepresentation convertToListRepresentation() {
		// Call the function to convert infix list to postfix list
		List<ExpressionAtom> newList = conversionFromInfix(infixExpression);

		// convert the expression list to ListRepresentation
		ListRepresentation occ = ExpressionToList(newList);

		// call the function to simplify the listRepresentation if there is a
		// need
		occ = simplifyUlti(occ);

		// check if the 1st operator is minus, if true, bring it to the filpSign
		// function,
		// which will do the work to flip - to + and make corresponding changes
		// to the whole representation
		if (occ.getNodeVal().getVariablesOrOperator().equals("-")
				&& occ.operands.size() == 2) {
			occ = flipSign(occ);
		}

		return occ;

	}

	private List<ExpressionAtom> conversionFromInfix(List<ExpressionAtom> list) {
		// This function convert the ExpressionAtom list from infix to postfix

		// local variable
		List<ExpressionAtom> oldList = new ArrayList<ExpressionAtom>();

		// put everything in the list to our local variable "oldList"
		for (int i = 0; i < list.size(); i++) {

			oldList.add(list.get(i));

		}

		// a list severed as a stack. add():push, pop():remove(size - 1),
		// peek():get(size - 1)
		List<ExpressionAtom> stack = new ArrayList<ExpressionAtom>();

		// this is the list that we are going to return
		List<ExpressionAtom> newList = new ArrayList<ExpressionAtom>();

		while (!oldList.isEmpty()) {

			ExpressionAtom newAtom = oldList.remove(0);

			if (newAtom.getAtomType() == AtomType.OPERATOR) {
				// Operator type.
				// If stack is not empty,
				// and the operator in the stack has lower precedence compared
				// to this one,
				// then remove it from stack and add it to output.
				while (!stack.isEmpty()
						&& !precedenceCheck(stack.get(stack.size() - 1),
								newAtom)) {
					newList.add(stack.remove(stack.size() - 1));
				}

				// bracket check. If true, pop until hits the nearest left
				// bracket in the stack
				if (newAtom.getVariablesOrOperator().equals(")")) {
					while (!stack.get(stack.size() - 1)
							.getVariablesOrOperator().equals("(")) {
						newList.add(stack.remove(stack.size() - 1));
					}
					stack.remove(stack.size() - 1);
				}
				// other operators, add to the stack;
				else {
					// a important step. Check if it is a unary minus.
					// If true, we change it to a binary minus, but add a "0" as
					// the first operand
					// "O" here is denoted as "$0"(a string) so that we don't
					// have to put a actual "0" to the coefficient,
					// which may cause problems afterwards
					if (newAtom.getVariablesOrOperator().equals("%")) {
						stack.add(new ExpressionAtom("-", AtomType.OPERATOR, 1));

						newList.add(new ExpressionAtom("$0", AtomType.OPERAND,
								1));

					} else {

						stack.add(newAtom);

					}

				}
			}
			// normal operand, directly add to the output
			else if (newAtom.getAtomType() == AtomType.OPERAND) {
				newList.add(newAtom);
			}

		}

		// pop all the things that are still left in the stack
		while (!stack.isEmpty()) {
			newList.add(stack.remove(stack.size() - 1));
		}

		return newList;

	}

	private ListRepresentation ExpressionToList(List<ExpressionAtom> list) {
		// This function convert the postfix ExpressionAtom list to the actual
		// listRepresentation

		// a list severed as a stack. add():push, pop():remove(size - 1),
		// peek():get(size - 1)
		List<ListRepresentation> stack = new ArrayList<ListRepresentation>();

		for (int i = 0; i < list.size(); i++) {
			// loop through the entire list and utilize stack to temporarily
			// store objects
			if (list.get(i).getAtomType() == AtomType.OPERATOR
					&& stack.size() != 0) {
				// an operator. popping two objects from the stack, and make
				// them into a listRepresentation
				ListRepresentation newOperator = new ListRepresentation();
				newOperator.setNodeVal(list.get(i));
				ListRepresentation op2 = stack.remove(stack.size() - 1);
				ListRepresentation op1 = stack.remove(stack.size() - 1);
				newOperator.operands.add(op1);
				newOperator.operands.add(op2);
				// we deal with minus sign right here,
				// so that it can be sured that the return list will have no
				// minus operator inside
				// call flipSign function, a function that can convert a minus
				// operator listRepresentation
				// to a plus operator listRepresentation which holds the same
				// result
				if (newOperator.getNodeVal().getVariablesOrOperator()
						.equals("-")) {
					newOperator = flipSign(newOperator);

				}

				stack.add(newOperator);

			} else {
				// operand. Add to the stack.
				ListRepresentation newOprand = new ListRepresentation();
				newOprand.setNegative(false);
				newOprand.setNodeVal(list.get(i));
				stack.add(newOprand);

			}

		}
		// After all the popping and combination, we will have actual list
		// sitting on the 1st index of the stack
		return stack.get(0);
	}

	private boolean precedenceCheck(ExpressionAtom a, ExpressionAtom b) {
		// Return true if expressionAtom a has lower precedence than b
		// () > ^ > * > +,-

		// a is "*"
		if (a.getVariablesOrOperator().equals("*")) {
			if (b.getVariablesOrOperator().equals("^")
					|| b.getVariablesOrOperator().equals("(")) {
				return true;
			} else {

				return false;
			}

		}
		// a is "^"
		else if (a.getVariablesOrOperator().equals("^")) {
			if (b.getVariablesOrOperator().equals("(")) {
				return true;
			} else {

				return false;
			}
		}
		// a is +/-
		if (a.getVariablesOrOperator().equals("+")
				|| a.getVariablesOrOperator().equals("-")) {

			if (b.getVariablesOrOperator().equals("+")
					|| b.getVariablesOrOperator().equals("-")) {
				return false;
			} else {

				return true;
			}

		}

		// a is left bracket
		else if (a.getVariablesOrOperator().equals("(")) {
			return true;
		}

		return false;
	}

	private ListRepresentation flipSign(ListRepresentation sub) {
		// A function flips "-" to "+" by doing negation to the second operand
		// this will be called recursively if there is a need

		if (sub.getNodeVal().getVariablesOrOperator().equals("^")) {
			// ^, Check if there is a need for reversing its "isNegative" value
			// by using isNegative and setNegative
			boolean boolValue = sub.isNegative();
			sub.setNegative(!boolValue);
		} else if (sub.getNodeVal().getVariablesOrOperator().equals("*")) {
			sub.operands.set(0, flipSign(sub.operands.get(0)));
		} else if (sub.getNodeVal().getVariablesOrOperator().equals("+")) {
			// +, negate everything inside
			for (int i = 0; i < sub.operands.size(); i++) {
				sub.operands.set(i, flipSign(sub.operands.get(i)));
			}
		} else if (sub.getNodeVal().getVariablesOrOperator().equals("-")) {
			// -, here we need to truly give a negation to the whole
			// representation
			// thus instead of using recursion, which will flip the sign but
			// equalize the result,
			// we will call our negation function "negation".
			// Still to remember to check for "$0", which is a fake zero that we
			// add in conversionFromInfix.
			sub.getNodeVal().setVariablesOrOperator("+");

			if (sub.operands.get(0).getNodeVal().getVariablesOrOperator()
					.equals("$0")) {
				sub.operands.remove(0);
				sub.operands.set(0, negation(sub.operands.get(0)));
				sub = simplifyUlti(sub);
			} else {
				sub.operands.set(1, negation(sub.operands.get(1)));

			}
		} else if (sub.getNodeVal().getAtomType() == AtomType.OPERAND) {
			// served as base case for recursion
			sub.getNodeVal().setCoefficient(
					sub.getNodeVal().getCoefficient() * (-1));
		}

		return sub;

	}

	private ListRepresentation negation(ListRepresentation sub) {
		// A function doing negation to a listRepresentation object

		if (sub.getNodeVal().getVariablesOrOperator().equals("-")) {
			sub.getNodeVal().setVariablesOrOperator("+");
			if (sub.operands.get(0).getNodeVal().getVariablesOrOperator()
					.equals("$0")) {
				sub.operands.remove(0);
				sub = simplifyUlti(sub);
			} else {
				// different from flipSign, which we negate the second operand
				// to equalize the result
				// here we negate the first operand to reverse the result
				sub.operands.set(0, negation(sub.operands.get(0)));
			}

		} else if (sub.getNodeVal().getVariablesOrOperator().equals("+")) {
			for (int i = 0; i < sub.operands.size(); i++) {
				sub.operands.set(i, negation(sub.operands.get(i)));
			}
		} else if (sub.getNodeVal().getVariablesOrOperator().equals("^")) {
			boolean boolValue = sub.isNegative();
			sub.setNegative(!boolValue);
		} else if (sub.getNodeVal().getVariablesOrOperator().equals("*")) {

			sub.operands.set(0, flipSign(sub.operands.get(0)));
		} else if (sub.getNodeVal().getAtomType() == AtomType.OPERAND) {
			sub.getNodeVal().setCoefficient(
					sub.getNodeVal().getCoefficient() * (-1));
		}

		return sub;
	}

	private ListRepresentation simplifyUlti(ListRepresentation lr) {
		// A function that will be always called when dealing with
		// listRepresentation object
		// basically it will simplify a listRepresentation in a throughout way
		// by merging the operator and its "operands" which might share the same
		// type of operator with it
		// for example: [+, [+, [a], [b]], c] to [+, [a], [b], [c]]

		int i = 0;
		int k = 0;
		for (; i < lr.operands.size(); i++) {
			if (lr.operands.get(i).getNodeVal().getAtomType() == AtomType.OPERATOR) {
				lr.operands.set(i, simplifyUlti(lr.operands.get(i)));
			}
		}

		if (lr.getNodeVal().getVariablesOrOperator().equals("+")
				&& lr.operands.size() == 1) {
			lr = lr.operands.get(0);
		}

		for (; k < lr.operands.size(); k++) {
			if (lr.operands.get(k).getNodeVal().getAtomType() == AtomType.OPERATOR
					&& lr.operands.get(k).getNodeVal().getVariablesOrOperator()
							.equals(lr.getNodeVal().getVariablesOrOperator())) {
				ListRepresentation traceBack = lr.operands.remove(k);

				while (!traceBack.operands.isEmpty()) {
					ListRepresentation substitude = traceBack.operands
							.remove(traceBack.operands.size() - 1);
					lr.operands.add(k, substitude);
				}
			}

			else {
				continue;

			}

		}

		return lr;
	}

	private ListRepresentation expandExponent(ListRepresentation lr) {
		// Turn power into the repeated multiplication of the bases

		int i = 0;
		for (; i < lr.operands.size(); i++) {
			// recursively calling to make sure that all the nested
			// listRepresentations have been through this procedure
			if (lr.operands.get(i).getNodeVal().getAtomType() == AtomType.OPERATOR) {
				lr.operands.set(i, expandExponent(lr.operands.get(i)));
			}
		}
		if (lr.getNodeVal().getVariablesOrOperator().equals("^")) {

			if (lr.operands.get(1).getNodeVal().getCoefficient() == 0) {
				lr.operands.clear();
				lr.setNodeVal(new ExpressionAtom("", AtomType.OPERAND, 1));
			} else {
				lr.getNodeVal().setVariablesOrOperator("*");
				int exponent = lr.operands.get(1).getNodeVal().getCoefficient();
				int j = 0;
				ListRepresentation base = lr.operands.get(0);
				for (; j < exponent - 1; j++) {
					lr.operands.add(0, base);
				}
				lr.operands.remove(lr.operands.size() - 1);

			}
		}

		return lr;
	}

	private ListRepresentation multipleCollapse(ListRepresentation lr) {
		// Collapse multiplication into polynomial additions

		int i = 0;
		for (; i < lr.operands.size(); i++) {
			// recursively calling to make sure that all the nested
			// listRepresentations have been through this procedure
			if (lr.operands.get(i).getNodeVal().getAtomType() == AtomType.OPERATOR) {
				lr.operands.set(i, multipleCollapse(lr.operands.get(i)));

			}
		}

		if (lr.getNodeVal().getVariablesOrOperator().equals("*")) {
			ExpressionAtom plus = new ExpressionAtom("+", AtomType.OPERATOR, 1);
			lr.setNodeVal(plus);

			while (lr.operands.size() != 1) {

				// call function "negativeCheck"
				boolean negativeOrNot = negativeCheck(lr.operands.get(0),
						lr.operands.get(1));

				// doing collapse in a binary way. 4 cases.
				// 1.both are operator
				if (lr.operands.get(0).getNodeVal().getAtomType() == AtomType.OPERATOR
						&& lr.operands.get(1).getNodeVal().getAtomType() == AtomType.OPERATOR) {
					int firstSize = lr.operands.get(0).operands.size();
					int secondSize = lr.operands.get(1).operands.size();
					ListRepresentation newEle = new ListRepresentation();
					newEle.setNegative(negativeOrNot);

					for (int m = 0; m < firstSize; m++) {
						int n = 0;
						for (; n < secondSize; n++) {
							ExpressionAtom expressOne = lr.operands.get(0).operands
									.get(m).getNodeVal();
							ExpressionAtom expressTwo = lr.operands.get(1).operands
									.get(n).getNodeVal();

							newEle.setNodeVal(plus);

							ExpressionAtom newExpression = new ExpressionAtom(
									expressOne.getVariablesOrOperator()
											+ expressTwo
													.getVariablesOrOperator(),
									AtomType.OPERAND, expressOne
											.getCoefficient()
											* expressTwo.getCoefficient());
							ListRepresentation newOprand = new ListRepresentation();
							newOprand.setNodeVal(newExpression);
							newEle.operands.add(newOprand);
						}
					}
					lr.operands.set(0, newEle);

				}

				// 2. Operand1 is a true operand, Operand2 is a operator
				else if (lr.operands.get(0).getNodeVal().getAtomType() == AtomType.OPERAND
						&& lr.operands.get(1).getNodeVal().getAtomType() == AtomType.OPERATOR) {
					int secondSize = lr.operands.get(1).operands.size();
					ExpressionAtom expressOne = lr.operands.get(0).getNodeVal();
					ListRepresentation newEle = new ListRepresentation();
					newEle.setNegative(negativeOrNot);
					newEle.setNodeVal(plus);

					for (int n = 0; n < secondSize; n++) {
						ExpressionAtom expressTwo = lr.operands.get(1).operands
								.get(n).getNodeVal();
						ExpressionAtom newExpression = new ExpressionAtom(
								expressOne.getVariablesOrOperator()
										+ expressTwo.getVariablesOrOperator(),
								AtomType.OPERAND, expressOne.getCoefficient()
										* expressTwo.getCoefficient());
						ListRepresentation newOprand = new ListRepresentation();
						newOprand.setNodeVal(newExpression);
						newEle.operands.add(newOprand);
					}

					lr.operands.set(0, newEle);

				}
				// 3. Operand2 is a true operand, Operand3 is a operator
				else if (lr.operands.get(0).getNodeVal().getAtomType() == AtomType.OPERATOR
						&& lr.operands.get(1).getNodeVal().getAtomType() == AtomType.OPERAND) {

					int firstSize = lr.operands.get(0).operands.size();
					ExpressionAtom expressTwo = lr.operands.get(1).getNodeVal();
					ListRepresentation newEle = new ListRepresentation();
					newEle.setNegative(negativeOrNot);
					newEle.setNodeVal(plus);

					for (int n = 0; n < firstSize; n++) {
						ExpressionAtom expressOne = lr.operands.get(0).operands
								.get(n).getNodeVal();
						ExpressionAtom newExpression = new ExpressionAtom(
								expressOne.getVariablesOrOperator()
										+ expressTwo.getVariablesOrOperator(),
								AtomType.OPERAND, expressOne.getCoefficient()
										* expressTwo.getCoefficient());
						ListRepresentation newOprand = new ListRepresentation();
						newOprand.setNodeVal(newExpression);
						newEle.operands.add(newOprand);
					}

					lr.operands.set(0, newEle);

				}
				// 4. both are true operands
				else if (lr.operands.get(0).getNodeVal().getAtomType() == AtomType.OPERAND
						&& lr.operands.get(1).getNodeVal().getAtomType() == AtomType.OPERAND) {
					ExpressionAtom expressOne = lr.operands.get(0).getNodeVal();
					ExpressionAtom expressTwo = lr.operands.get(1).getNodeVal();

					ExpressionAtom newExpression = new ExpressionAtom(
							expressOne.getVariablesOrOperator()
									+ expressTwo.getVariablesOrOperator(),
							AtomType.OPERAND, expressOne.getCoefficient()
									* expressTwo.getCoefficient());
					ListRepresentation newOprand = new ListRepresentation();
					newOprand.setNodeVal(newExpression);
					newOprand.setNegative(negativeOrNot);
					lr.operands.set(0, newOprand);
				}

				lr.operands.remove(1);
			}

		}

		// first we check if this listRepresentation has isNeagtive set to
		// true.(Corner case)
		// If true, we do negation to the whole listRepresention
		// then we use simplifyUlti to merge the whole listRepresentation from
		// inside out if there is a need
		if (lr.isNegative() == true) {
			lr.setNegative(false);
			if (lr.getNodeVal().getAtomType() == AtomType.OPERAND) {
				lr.getNodeVal().setCoefficient(
						lr.getNodeVal().getCoefficient() * -1);
			} else {
				for (int f = 0; f < lr.operands.size(); f++) {
					lr = negation(lr);
				}
			}
		}

		lr = simplifyUlti(lr);
		return lr;
	}

	private boolean negativeCheck(ListRepresentation posZero,
			ListRepresentation posOne) {
		// A function that will decide if both of the parameter
		// listRepresentations have set the isNegative to true, or both set it
		// to false.
		// If this is the case, return true, meaning that their multiplication
		// product should set isNegative to false

		if ((posZero.isNegative() == false && posOne.isNegative() == false)
				|| (posZero.isNegative() == true && posOne.isNegative() == true)) {
			return false;
		} else {
			return true;
		}
	}

	private List<ExpressionAtom> evaluateExpression() {
		// Two steps: first reduce power notation to multiplication
		// then collapse multiplication in to addition

		listRepresentation = expandExponent(listRepresentation);
		listRepresentation = multipleCollapse(listRepresentation);
		// simplifyUlti(listRepresentation);

		// Convert the listRepresentation to the list of expressionAtom
		List<ExpressionAtom> list = new ArrayList<ExpressionAtom>();
		if (listRepresentation.operands.size() > 0) {
			for (int i = 0; i < listRepresentation.operands.size(); i++) {
				list.add(listRepresentation.operands.get(i).getNodeVal());
			}
		} else {
			list.add(listRepresentation.getNodeVal());
		}

		return list;

	}

	private List<ExpressionAtom> simplifyAndNormalize(
			List<ExpressionAtom> evaluatedExpression) {
		// In this function, we merge the expressionAtom with the same variable
		// type but different coefficients.

		if (evaluatedExpression.size() > 1) {
			// Introducing a boolean array to the procedure, which has the same
			// length as expressionAtom list. This boolean array will keep
			// tracking all the corresponding indexes in the expressionAtom list
			// One boolean element will be set to false if a element in
			// expressionAtom list of corresponding index has been merged with
			// others, so that we won't repeat merging on the same element by
			// mistake
			List<ExpressionAtom> newList = new ArrayList<ExpressionAtom>();
			boolean[] dirtyValue = new boolean[evaluatedExpression.size()];
			Arrays.fill(dirtyValue, true);

			for (int i = 0; i < evaluatedExpression.size(); i++) {
				if (dirtyValue[i] == true) {
					ExpressionAtom newAtom = evaluatedExpression.get(i);

					for (int j = i + 1; j < evaluatedExpression.size(); j++) {
						if (dirtyValue[j] == true) {
							if (!evaluatedExpression.get(i)
									.getVariablesOrOperator().isEmpty()
									&& !evaluatedExpression.get(j)
											.getVariablesOrOperator().isEmpty()
									&& evaluatedExpression.get(i)
											.getVariablesOrOperator().length() == evaluatedExpression
											.get(j).getVariablesOrOperator()
											.length()) {
								// calling "compareTerms" to see if the two
								// expression have the same variable
								if (compareTerms(evaluatedExpression.get(i)
										.getVariablesOrOperator(),
										evaluatedExpression.get(j)
												.getVariablesOrOperator()) == true) {

									newAtom.setCoefficient(newAtom
											.getCoefficient()
											+ evaluatedExpression.get(j)
													.getCoefficient());
									dirtyValue[j] = false;
								}

							}
							// a situation that both are constant values
							else if (evaluatedExpression.get(i)
									.getVariablesOrOperator().isEmpty()
									&& evaluatedExpression.get(j)
											.getVariablesOrOperator().isEmpty()) {

								newAtom.setCoefficient(newAtom.getCoefficient()
										+ evaluatedExpression.get(j)
												.getCoefficient());
								dirtyValue[j] = false;
							} else {
								continue;
							}
						}
					}
					newList.add(newAtom);
					dirtyValue[i] = false;
				}

			}
			return newList;
		} else {

			return evaluatedExpression;
		}

	}

	private boolean compareTerms(String one, String two) {
		// a function using array.sorts and arrays.equal to determine if the two
		// string contain the same letters in them but may just be arranged in
		// different order
		char[] firstArray = one.toCharArray();
		char[] secondArray = two.toCharArray();

		Arrays.sort(firstArray);
		Arrays.sort(secondArray);

		boolean equal = false;

		if (Arrays.equals(firstArray, secondArray)) {
			equal = true;
		} else {
			equal = false;
		}

		return equal;
	}

	public Polynomial(String inputPolynomial) {
		this.infixExpression = parseInputPolynomial(inputPolynomial);

		this.listRepresentation = convertToListRepresentation();
	}

	private String sortString(String termVars) {
		char[] ar = termVars.toCharArray();
		Arrays.sort(ar);
		return String.valueOf(ar);
	}

	public void evaluate() {
		List<ExpressionAtom> evaluatedExpression = evaluateExpression();

		for (int i = 0; i < evaluatedExpression.size(); ++i) {
			evaluatedExpression.get(i).setVariablesOrOperator(
					(sortString(evaluatedExpression.get(i)
							.getVariablesOrOperator())));
		}

		this.finalExpression = simplifyAndNormalize(evaluatedExpression);
	}
}