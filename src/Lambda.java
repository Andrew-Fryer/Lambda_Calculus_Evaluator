import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class Lambda implements Term, Serializable {
	private static final long serialVersionUID = 1L;  // eclipse did this
	public Variable var = new Variable();
	public Expression body = new Expression();
	
	public static Lambda parseLambda(String input, Map<String, Variable> varNameMap) {
		return new Lambda();
	}
	
	public void substitute(Term input) {
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
		
		return "(\\" + var.name + (var.value != null ? "[:= " + var.value.stringify() + "]" : "") + " -> " + body.stringify() + ")";
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
		/*Lambda clone = new Lambda();
		
		clone.var.name = this.var.name;
		clone.var.numRefs = this.var.numRefs;
		if(this.var.value == null) {
			clone.var.value = null;
		} else if(this.var.value instanceof Instance) {
			clone.var.value = new Instance();
			clone.var.value.binding = 
		}
		
		return ;*/
	}

}
