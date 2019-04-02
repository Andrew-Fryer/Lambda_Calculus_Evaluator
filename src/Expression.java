import java.util.Stack;

public class Expression {
	public Stack<Term> terms  = new Stack<Term>();
	private boolean isSimplified = false;
	
	public static Expression parseInput(String input) {
		// TODO: actually do the work here
		return new Expression();
	}
	
	void simplify() {
		System.out.println("here is the current state of the current expression: ");
		System.out.println(stringify());
		// should eating set isSimplified to false?
		if(isSimplified) {
			//return;  // don't optimize when debugging
		}
		Lambda head = null;
		while(terms.size() > 1) {
			Term current = terms.pop();
			// extract lambda from any Instance shells
			while(current instanceof Instance) {
				try {
					current = ((Instance) current).binding.value;
					if(current == null) {
						// the variable is not defined yet
						isSimplified = true;
						return;
					}
				} catch (Exception e) {
					throw new Error("undefined instance");
				}
			}
			Lambda lambda = (Lambda) current;
			
			lambda.simplify();
			
			// clone the lambda so that we don't substitute into every instance of the variable
			try {
				head = (Lambda) lambda.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			head.eat(terms.pop());
			terms.push(head);
		}
		isSimplified = true;
		System.out.println("and here it is now: ");
		System.out.println(stringify());
	}
	
	public String stringify() {
		String result = "";
		for(int i=0; i<terms.size(); i++) {
			result = result.concat(terms.get(i).stringify() + " ");
		}
		return result;
	}
}
