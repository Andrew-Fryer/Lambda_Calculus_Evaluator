import java.io.Serializable;
import java.util.Stack;
import java.util.Hashtable;
import java.util.Map;

public class Expression implements Serializable {
	private static final long serialVersionUID = 1L;
	public Stack<Term> terms  = new Stack<Term>();
	private boolean isSimplified = false;
	
	public static Expression createExpressionFromString(String input) {
		return new Expression(input, new Hashtable<String,Variable>());
	}

	public Expression(String input, Map<String, Variable> varNameMap) {
		
		int bufferLength = 0;
		
		int bracketPos = -1;
		int bracketDepth = 0;
		
		for(int i = 0; i < input.length(); i++) {
			char current = input.charAt(i);
			
			if(current == '(') {  // set bufferLength = 0?
				if(bracketDepth == 0) {
					bracketPos = i;
				}
				bracketDepth++;
			} else if(bracketDepth == 0) {
				if(current == ' ') {
					// flush the buffer
					if(bufferLength > 0) {
						String varName = input.substring(i - bufferLength, i);
						Instance inst = new Instance(varName, varNameMap);
						terms.push(inst);
						bufferLength = 0;
					}
				} else {
					bufferLength++;
				}
			} else if(current == ')') {  // I should really delete variables from the map when they go out of scope...
				if(bracketDepth <= 0) {
					throw new Error("closing bracket does not have any corresponding opening bracket");
				} else if(bracketDepth == 1) {
					String segment = input.substring(bracketPos+1, i);
					Term term;
					if(segment.charAt(0) == '\\') {
						Lambda lambda = new Lambda(segment, varNameMap);  // rename to parseLambda?
						term = (Term) lambda;
					} else {
						Expression expr = new Expression(segment, varNameMap);
						term = (Term) expr;
					}
					terms.push(term);  // I think this is using the stack as if the input string is in reverse polish
				}
				bracketDepth--;  // set bufferLength = 0?
			} // else do nothing because we are just skipping over the contents of something in brackets
		}
		// flush the buffer
		if(bufferLength > 0) {
			String varName = input.substring(input.length() - bufferLength);
			Instance inst = new Instance(varName, varNameMap);
			terms.push(inst);
		}
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
				lambda = (Lambda) ((Lambda) current).clone();
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
