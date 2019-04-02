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
		while(terms.size() > 1) {
			Term headTerm = terms.pop();
			Term current = headTerm;
			// extract lambda from any Instance shells
			while(current instanceof Instance) {
				current = ((Instance) current).binding.value;
				if(current != null) {
					current.simplify();
					try {
						current = (Term) ((Lambda) current).clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// the variable is not defined yet
					terms.push(headTerm);
					isSimplified = true;
					return;
				}
			}
			Lambda lambda = (Lambda) current;
			
			lambda.simplify();
			
			lambda.eat(terms.pop());
			
			lambda.simplify(); // forces the substitution
			
			terms.push(lambda);
		}
		isSimplified = true;
		System.out.println("and here it is now: ");
		System.out.println(stringify());
	}
	
	public String stringify() {
		String result = "";
		for(int i=terms.size()-1; i>=0; i--) {
			result = result.concat(terms.get(i).stringify() + " ");
		}
		return result;
	}
}
