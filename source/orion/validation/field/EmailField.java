package orion.validation.field;

public class EmailField extends RegexField {

	public EmailField(String name, Object value, String message) {
		super(name, value, message);
		regex = "\\b^['_a-z0-9-\\+]+(\\.['_a-z0-9-\\+]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2}|aero|arpa|asia|biz|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|nato|net|org|pro|tel|travel|xxx)$\\b";
		strip = true;
		caseSensitive = true;
	}

	public EmailField(String name, Object value) {
		this(name, value, null);
	}

	public RegexField setRegex(String regex) {
		throw new RuntimeException("Not allowed to set Regular Expression in EmailField");
	}

	public RegexField setCaseSensitive(boolean caseSensitive) {
		throw new RuntimeException("Not allowed to set Case Sensitivity in EmailField");
	}

	public EmailField setRequired(boolean required) {
		this.required = required;
		return this;
	}

}
