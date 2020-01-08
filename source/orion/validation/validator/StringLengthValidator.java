package orion.validation.validator;

import orion.controller.Notification;
import orion.core.Utility;
import orion.validation.field.StringLengthField;

public class StringLengthValidator {

	public static boolean validate(Notification notification, StringLengthField... fields) {
		for (StringLengthField field : fields) {
			int min = field.getMin();
			int max = field.getMax();

			if (field.getMessage() == null) {
				String message = "Field " + field.getName() + " ";
				if (min > -1 && max > -1) {
					message += "must be between " + min + " and " + max + " in length";
				} else if (min > -1) {
					message += "must be greater than " + min + " in length";
				} else if (max > -1) {
					message += "must be less than " + max + " in length";
				} else {
					message += "is required";
				}
				field.setMessage(message);
			}

			if (!(field.getValue() instanceof String)) {
				if (field.isRequired()) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				}
				break;
			}

			String value = (String) field.getValue();

			if (field.isStrip()) {
				value = Utility.stripText(value);
			}

			if (min > -1 && (value.length() < min)) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}
			if (max > -1 && (value.length() > max)) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}
		}
		return true;
	}

}
