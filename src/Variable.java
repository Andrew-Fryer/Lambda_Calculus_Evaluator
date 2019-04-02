import java.io.Serializable;
import java.util.ArrayList;

public class Variable implements Serializable{
	private static final long serialVersionUID = 1L;
	public Term value;
	public String name;
	public int numRefs = 0;
}
