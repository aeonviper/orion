package orion.validation.field;

public class DoubleRangeField extends ValidationField {

	protected Double maxInclusive;
	protected Double minInclusive;
	protected Double minExclusive;
	protected Double maxExclusive;

	public DoubleRangeField(String name, Object value, String message) {
		super(name, value, message);
	}

	public DoubleRangeField(String name, Object value) {
		this(name, value, null);
	}

	public Double getMaxInclusive() {
		return maxInclusive;
	}

	public DoubleRangeField setMaxInclusive(Double maxInclusive) {
		this.maxInclusive = maxInclusive;
		return this;
	}

	public Double getMinInclusive() {
		return minInclusive;
	}

	public DoubleRangeField setMinInclusive(Double minInclusive) {
		this.minInclusive = minInclusive;
		return this;
	}

	public Double getMinExclusive() {
		return minExclusive;
	}

	public DoubleRangeField setMinExclusive(Double minExclusive) {
		this.minExclusive = minExclusive;
		return this;
	}

	public Double getMaxExclusive() {
		return maxExclusive;
	}

	public DoubleRangeField setMaxExclusive(Double maxExclusive) {
		this.maxExclusive = maxExclusive;
		return this;
	}

	public DoubleRangeField setRequired(boolean required) {
		this.required = required;
		return this;
	}
}
