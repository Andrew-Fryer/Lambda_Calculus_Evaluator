import java.io.Serializable;

public class Instance implements Term, Serializable {
	private static final long serialVersionUID = 1L;
	public Variable binding;

	public void simplify() {
		// do nothing
	}
	
	public String stringify() {
		return binding.name;
	}

}
