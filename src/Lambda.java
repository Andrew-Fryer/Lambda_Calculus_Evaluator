
public class Lambda implements Term, Cloneable {
	public Variable var = new Variable();
	public Expression body = new Expression();
	
	public static Lambda parseInput(String input) {
		return new Lambda();
	}
	
	public void substitute(Term input) {
		if(var.instances.size() > 0) {
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
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	

}
