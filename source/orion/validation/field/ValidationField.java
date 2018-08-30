package orion.validation.field;

public class ValidationField {

	protected String name;
	protected Object value;
	protected String message;
	protected boolean required = true;

	public ValidationField(String name, Object value, String message) {
		this.name = name;
		this.value = value;
		this.message = message;
	}

	public ValidationField(String name, Object value) {
		this(name, value, null);
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRequired() {
		return required;
	}

}
