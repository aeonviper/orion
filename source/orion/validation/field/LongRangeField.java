package orion.validation.field;

public class LongRangeField extends GenericRangeField<Long> {

	public LongRangeField(String name, Object value, String message) {
		super(name, value, message);
	}

	public LongRangeField(String name, Object value) {
		this(name, value, null);
	}

}
