package orion.validation.field;

public class ShortRangeField extends GenericRangeField<Short> {

	public ShortRangeField(String name, Object value, String message) {
		super(name, value, message);
	}

	public ShortRangeField(String name, Object value) {
		this(name, value, null);
	}

}
