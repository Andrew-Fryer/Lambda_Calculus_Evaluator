import java.io.Serializable;
import java.util.Map;

public class Instance implements Term, Serializable {
	private static final long serialVersionUID = 1L;
	public Variable binding;
	
	public Instance(String input, Map<String, Variable> varNameMap) {
		binding = varNameMap.get(input);
		if(binding == null) {
			throw new Error("Undefined variable: \"" + input + "\"");
		}
		binding.numRefs++;
	}

	public void simplify() {
		// do nothing
	}
	
	public String stringify() {
		String toDisplay;
		if(binding.value == null) {
			toDisplay = binding.name;
		} else {
			toDisplay = binding.value.stringify();
		}
		return toDisplay;
	}

}
