package orion.validation.field;

public class GenericRangeField<T extends Comparable> extends ValidationField {

	protected T min;
	protected T max;

	public GenericRangeField(String name, Object value, String message) {
		super(name, value, message);
	}

	public GenericRangeField(String name, Object value) {
		this(name, value, null);
	}

	public T getMin() {
		return min;
	}

	public GenericRangeField<T> setMin(T min) {
		this.min = min;
		return this;
	}

	public T getMax() {
		return max;
	}

	public GenericRangeField<T> setMax(T max) {
		this.max = max;
		return this;
	}

	public GenericRangeField<T> setRequired(boolean required) {
		this.required = required;
		return this;
	}

}
