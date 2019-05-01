package orion.validation.field;

import java.util.Date;

public class DateRangeField extends GenericRangeField<Date> {

	public DateRangeField(String name, Object value, String message) {
		super(name, value, message);
	}

	public DateRangeField(String name, Object value) {
		this(name, value, null);
	}

}
