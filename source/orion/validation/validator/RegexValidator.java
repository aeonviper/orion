package orion.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import orion.controller.Notification;
import orion.core.Utility;
import orion.validation.field.RegexField;

public class RegexValidator {

	public static boolean validate(Notification notification, RegexField... fields) {
		for (RegexField field : fields) {

			if (field.getMessage() == null) {
				field.setMessage("Field " + field.getName() + " must match regular expression " + field.getRegex());
			}

			if (!(field.getValue() instanceof String)) {
				if (field.isRequired()) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				}
				break;
			}

			String regex = field.getRegex();
			if (regex == null) {
				break;
			}

			String value = (String) field.getValue();

			if (field.isStrip()) {
				value = Utility.stripText(value);
			}

			if (value.isEmpty()) {
				if (field.isRequired()) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				}
				break;
			}

			Pattern pattern;
			if (field.isCaseSensitive()) {
				pattern = Pattern.compile(regex);
			} else {
				pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			}

			Matcher matcher = pattern.matcher(value);

			if (!matcher.matches()) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}

		}
		return true;
	}

}
