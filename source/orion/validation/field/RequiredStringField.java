package orion.validation.field;

public class RequiredStringField extends ValidationField {

	protected boolean strip = true;

	public RequiredStringField(String name, Object value, String message) {
		super(name, value, message);
	}

	public RequiredStringField(String name, Object value) {
		this(name, value, null);
	}

	public boolean isStrip() {
		return strip;
	}

	public RequiredStringField setStrip(boolean strip) {
		this.strip = strip;
		return this;
	}

}
