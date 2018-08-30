package orion.validation.field;

public class IntegerRangeField extends GenericRangeField<Integer> {

	public IntegerRangeField(String name, Object value, String message) {
		super(name, value, message);
	}

	public IntegerRangeField(String name, Object value) {
		this(name, value, null);
	}

}
