package orion.validation.field;

public class RequiredField extends ValidationField {

	public RequiredField(String name, Object value, String message) {
		super(name, value, message);
	}

	public RequiredField(String name, Object value) {
		this(name, value, null);
	}

}
