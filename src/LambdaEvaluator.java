
public class LambdaEvaluator {

	public static void main(String[] args) {
		
		/*
		// this is my test data:
		
		Lambda lambda1 = new Lambda();
		
		lambda1.var.name = "y";
		
		Instance inst = new Instance();
		
		inst.binding = lambda1.var;
		
		Instance inst3 = new Instance();
		
		inst3.binding = lambda1.var;
		
		//lambda1.var.instances.add(inst);
		
		lambda1.body.terms.add(inst);
		
		//lambda1.var.instances.add(inst3);
		lambda1.var.numRefs = 2;
		
		lambda1.body.terms.add(inst3);
		
		Expression expr1 = new Expression();
		
		expr1.terms.add(lambda1);
		
		String output = expr1.stringify();
		
		System.out.println(output);
		
		
		
		Lambda lambda2 = new Lambda();
		
		lambda2.var.name = "x";
		
		Instance inst2 = new Instance();
		
		inst2.binding = lambda2.var;
		
		Instance inst4 = new Instance();
		
		inst4.binding = lambda2.var;
		
		//lambda2.var.instances.add(inst2);
		
		lambda2.body.terms.add(inst2);
		
		//lambda2.var.instances.add(inst4);
		lambda2.var.numRefs = 2;
		
		lambda2.body.terms.add(inst4);
		
		expr1.terms.add(lambda2);
		
		output = expr1.stringify();
		
		System.out.println(output);
		
		
		
		expr1.simplify();
		
		output = expr1.stringify();
		
		System.out.println(output);
		
		System.out.println("hello world");
		
		*/
		
		// remember that the order of expression looks backwards in the debugger because index 0 is the bottom of the stack (the right-most term)
		Expression exprFromString = Expression.createExpressionFromString("(\\m -> (\\n -> (\\f -> m (n f)))) (\\h -> (\\z -> h (h z))) (\\g -> (\\y -> g (g (g y))))");
		
		exprFromString.simplify();
		
		System.out.println(exprFromString.stringify());
	}
}
