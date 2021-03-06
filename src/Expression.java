import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Stack;
import java.util.Hashtable;
import java.util.Map;

public class Expression implements Term, Serializable, Printable {
	private static final long serialVersionUID = 1L;
	public Stack<Term> terms  = new Stack<Term>();
	private boolean isSimplified = false;
	private Printable root;
	
	public static Expression createExpressionFromString(String input) {
		return new Expression(input, new Hashtable<String,Variable>(), null);
	}
	
	public Expression() {
		// used by the clone
	}

	public Expression(String input, Map<String, Variable> varNameMap, Printable root) {
		if(root == null) {
			root = this;
		}
		this.root = root;
		
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
						terms.add(0, inst);
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
						Lambda lambda = new Lambda(segment, varNameMap, root);  // rename to parseLambda?
						term = (Term) lambda;
					} else {
						Expression expr = new Expression(segment, varNameMap, root);
						term = (Term) expr;
					}
					terms.add(0, term);  // I think this is using the stack as if the input string is in reverse polish
				}
				bracketDepth--;  // set bufferLength = 0?
			} // else do nothing because we are just skipping over the contents of something in brackets
		}
		// flush the buffer
		if(bufferLength > 0) {
			String varName = input.substring(input.length() - bufferLength);
			Instance inst = new Instance(varName, varNameMap);
			terms.add(0, inst);
		}
	}

	public void simplify() {
		// should eating set isSimplified to false?
		if(isSimplified) {
			//return;  // don't optimize when debugging
		}
		
		while(terms.size() > 0) {
			System.out.println("Simplifying: " + this.stringify());
			System.out.println("root looks like: ");
			root.print();
			
			Term term = terms.peek();
			
			Term current = term;
			Instance prevInstance = null;
			// extract lambda from any Instance shells
			boolean unwrappedAndSimplified = false;
			while(!unwrappedAndSimplified) {
				if(current == null) {
					// instance cannot be unwrapped any farther
					term = prevInstance;
					// the instance has not been assigned a value
					System.out.println("The following variable has not been assigned a value: " + ((Instance)term).binding.name);
					return;  // without a value, the expression cannot be simplified any more
				} else if(current instanceof Instance) {
					// unwrap instance
					prevInstance = (Instance) current;
					current = prevInstance.binding.value;
				} else if(current instanceof Lambda) {
					// unwrapped to a Lambda
					term = (Lambda) ((Lambda) current).clone();
					Lambda lambda = (Lambda) term;
					
					lambda.simplify();  // simplify before cloning?
					
					if(terms.size() > 1) {
						lambda.substitute(terms.get(terms.size()-2));
						
						// there shouldn't be any instances of the outermost variable in the body anymore
						terms.pop();  // discard lambda
						terms.pop();  // discard whatever was substituted
						terms.addAll(lambda.body.terms);
						unwrappedAndSimplified = true;
					} else {
						isSimplified = true;
						return;
					}
				} else if(current instanceof Expression) {
					// unwrapped to an Expression
					current.simplify();
					term = (Expression) ((Expression) current).clone();
					Expression expr = (Expression) term;
					if(expr.terms.size() == 1) {
						// unwrap Expression
						current = expr.terms.firstElement();
					} else {
						return;
					}
				}
			}
			
			System.out.println("Simplified to: " + this.stringify());
			System.out.println("root looks like: ");
			root.print();
		}
	}
	
	public String stringify() {
		String result = "(";
		for(int i = terms.size()-1; i >= 0; i--) {
			result = result.concat(terms.get(i).stringify() + " ");
		}
		return result + ")";
	}
	
	public void print() {
		System.out.println(stringify());
	}
	
	public Object clone() {
		try {
			ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
			ObjectOutputStream outputObjectStream = new ObjectOutputStream(outputByteStream);
			outputObjectStream.writeObject(this);
			ByteArrayInputStream inputByteStream = new ByteArrayInputStream(outputByteStream.toByteArray());
			ObjectInputStream inputObjectStream = new ObjectInputStream(inputByteStream);
			return inputObjectStream.readObject();
		} catch (Exception e) {
			throw new Error("Failed to clone lambda");
		}
	}
}
