package orion.validation.field;

public class URLField extends ValidationField {

	public URLField(String name, Object value, String message) {
		super(name, value, message);
	}

	public URLField(String name, Object value) {
		this(name, value, null);
	}

	public URLField setRequired(boolean required) {
		this.required = required;
		return this;
	}

}
