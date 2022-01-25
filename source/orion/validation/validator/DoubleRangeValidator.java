package orion.validation.validator;

import orion.controller.Notification;
import orion.validation.field.DoubleRangeField;

public class DoubleRangeValidator {

	public static boolean validate(Notification notification, DoubleRangeField... fields) {
		for (DoubleRangeField field : fields) {

			Double maxInclusive = field.getMaxInclusive();
			Double minInclusive = field.getMinInclusive();
			Double maxExclusive = field.getMaxExclusive();
			Double minExclusive = field.getMinExclusive();

			if (!(field.getValue() instanceof Double)) {
				if (field.isRequired()) {
					if (field.getMessage() != null) {
						notification.addFieldError(field.getName(), field.getMessage());
						return false;
					} else {
						notification.addFieldError(field.getName(), "Field " + field.getName() + " is required and must be a double");
						return false;
					}
				}
				continue;
			}

			Double value = (Double) field.getValue();

			if (maxInclusive != null && value.compareTo(maxInclusive) > 0) {
				if (field.getMessage() != null) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				} else {
					notification.addFieldError(field.getName(), "Field " + field.getName() + " cannot be greater than " + maxInclusive);
					return false;
				}
			}

			if (minInclusive != null && value.compareTo(minInclusive) < 0) {
				if (field.getMessage() != null) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				} else {
					notification.addFieldError(field.getName(), "Field " + field.getName() + " cannot be less than " + minInclusive);
					return false;
				}
			}
			if (maxExclusive != null && value.compareTo(maxExclusive) >= 0) {
				if (field.getMessage() != null) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				} else {
					notification.addFieldError(field.getName(), "Field " + field.getName() + " cannot be greater than or equal to " + maxExclusive);
					return false;
				}
			}

			if (minExclusive != null && value.compareTo(minExclusive) <= 0) {
				if (field.getMessage() != null) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				} else {
					notification.addFieldError(field.getName(), "Field " + field.getName() + " cannot be less than or equal to " + minExclusive);
					return false;
				}
			}

		}
		return true;
	}

}
