import java.io.Serializable;
import java.util.Map;

public class Instance implements Term, Serializable {
	private static final long serialVersionUID = 1L;
	public Variable binding;
	
	public static Instance parseInstance(String input, Map<String, Variable> varNameMap) {
		Instance result = new Instance();
		Variable binding = varNameMap.get(input);
		if(binding == null) {
			throw new Error("Undefined variable: \"" + input + "\"");
		}
		result.binding = binding;
		binding.numRefs++;
		
		return result;
	}

	public void simplify() {
		// do nothing
	}
	
	public String stringify() {
		return binding.name;
	}

}
