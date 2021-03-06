import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class Lambda implements Term, Serializable {
	private static final long serialVersionUID = 1L;  // eclipse did this
	public Variable var;
	public Expression body;
	private Printable root;
	
	public Lambda(String input, Map<String, Variable> varNameMap, Printable root) {
		this.root = root;
		
		int i = 0;
		i++; // move to after the '\\' character
		// ignore whitespace
		while(input.charAt(i) == ' ') {
			i++;
		}
		int varNameStart = i;
		while(input.charAt(i) != ' ' ) {  // && != '-'
			i++;
		}
		var = new Variable();
		var.name = input.substring(varNameStart, i);
		varNameMap.put(var.name,  var);
		
		// ignore whitespace
		while(input.charAt(i) == ' ') {
			i++;
		}
		i += 2;  // jump over "->"
		// ignore whitespace
		while(input.charAt(i) == ' ') {
			i++;
		}
		body = new Expression(input.substring(i, input.length()), varNameMap, root);
	}
	
	public void substitute(Term input) {  // input could be an instance, a lambda, or an expression
		if(var.numRefs > 0) {
			input.simplify();
		} else {
			// should I somehow mark that input is garbage?
		}
		var.value = input;
		// body.isSimplified = false;
		body.simplify();  // this forces substitution (in the unwrap helper function)
		// why not just have the unwrap method here? -> because that wouldn't be lazy
		// could I implement unwrap as a getter method?
	}
	
	public void simplify() {
		// TODO Auto-generated method stub
		body.simplify();
	}
	
	public String stringify() {
		// TODO Auto-generated method stub
		
		return "(\\" + var.name + " -> " + body.stringify() + ")";
	}
	
	public Object clone() {  // deep
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
