package orion.validation.field;

public class RegexField extends ValidationField {

	protected boolean strip = true;
	protected String regex;
	protected boolean caseSensitive = true;

	public RegexField(String name, Object value, String message) {
		super(name, value, message);
	}

	public RegexField(String name, Object value) {
		this(name, value, null);
	}

	public boolean isStrip() {
		return strip;
	}

	public RegexField setStrip(boolean strip) {
		this.strip = strip;
		return this;
	}

	public String getRegex() {
		return regex;
	}

	public RegexField setRegex(String regex) {
		this.regex = regex;
		return this;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public RegexField setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this;
	}

	public RegexField setRequired(boolean required) {
		this.required = required;
		return this;
	}

}
