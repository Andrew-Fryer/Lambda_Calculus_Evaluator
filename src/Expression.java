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
		while(terms.size() > 0) {
			Term term = unwrapInstance(terms.pop());
			
			if(term instanceof Instance) {
				terms.push(term);
				return;
			}
			
			Lambda lambda = (Lambda) term;
			
			lambda.simplify();
			
			if(terms.size() > 0) {
				lambda.substitute(terms.pop());
			}
			
			// there shouldn't be any instances of the outermost variable in the body anymore
			
			terms.addAll(lambda.body.terms);  // check that this doesn't reverse order.....
		}
		isSimplified = true;
		System.out.println("and here it is now: ");
		System.out.println(stringify());
	}
	
	private static Term unwrapInstance(Term term) {
		Term current = term;
		Instance prevInstance;
		// extract lambda from any Instance shells
		while(current instanceof Instance) {
			prevInstance = (Instance) current;
			current = prevInstance.binding.value;
			if(current == null) {
				// the variable is not defined yet
				return prevInstance;
			} else if(current instanceof Lambda) {
				// we had to un-wrap an instance
				// now we need to clone it so that each instance has its own copy of the value of the variable
				Lambda lambda = null;
				try {
					lambda = (Lambda) ((Lambda) current).clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return lambda;
			}
		}
		return (Lambda) term;
	}
	
	public String stringify() {
		String result = "";
		for(int i=terms.size()-1; i>=0; i--) {
			result = result.concat(terms.get(i).stringify() + " ");
		}
		return result;
	}
}
