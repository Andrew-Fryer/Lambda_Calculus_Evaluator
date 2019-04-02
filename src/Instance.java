import java.io.Serializable;

public class Instance implements Term, Serializable {
	private static final long serialVersionUID = 1L;
	public Variable binding;

	public void simplify() {
		// TODO Auto-generated method stub
		
		// if the contents of the variable have not been simplified, simplify them?
	}
	
	public String stringify() {
		// TODO Auto-generated method stub
		
		return binding.name;
	}

}
