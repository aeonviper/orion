package orion.validation.field;

public class StringLengthField extends ValidationField {

	protected boolean strip = true;
	protected int max = -1;
	protected int min = -1;

	public StringLengthField(String name, Object value, String message) {
		super(name, value, message);
	}

	public StringLengthField(String name, Object value) {
		this(name, value, null);
	}

	public boolean isStrip() {
		return strip;
	}

	public StringLengthField setStrip(boolean strip) {
		this.strip = strip;
		return this;
	}

	public int getMax() {
		return max;
	}

	public StringLengthField setMax(int max) {
		this.max = max;
		return this;
	}

	public int getMin() {
		return min;
	}

	public StringLengthField setMin(int min) {
		this.min = min;
		return this;
	}

	public StringLengthField setRequired(boolean required) {
		this.required = required;
		return this;
	}

}
