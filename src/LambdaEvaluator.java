import java.util.Scanner;

public class LambdaEvaluator {

	public static void main(String[] args) {
		
		// remember that the order of expression looks backwards in the debugger because index 0 is the bottom of the stack (the right-most term)
		
		// This is a multiplication of the church numeral 2 by the church numeral 3
		Expression exprFromString = Expression.createExpressionFromString("(\\m -> (\\n -> (\\f -> m (n f)))) (\\h -> (\\z -> h (h z))) (\\g -> (\\y -> g (g (g y))))");
		exprFromString.simplify();
		System.out.println(exprFromString.stringify());
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("\n\nWelcome to the Lambda Calculus Evaluator!\nPlease enter an expression to simplify. Enter \":q\" to quit.");
		String input = scanner.nextLine();
		while(!input.equals(":q")) {
			Expression expr = Expression.createExpressionFromString(input);
			expr.simplify();
			System.out.println("");
			System.out.println("The simplified expression is:");
			expr.print();

			System.out.println("Please enter an expression to simplify. Enter \":q\" to quit.");
			input = scanner.nextLine();
		}
		scanner.close();
	}
}
