package orion.validation.validator;

import orion.controller.Notification;
import orion.validation.field.GenericRangeField;

public class GenericRangeValidator<T extends Comparable> {

	public boolean validate(Notification notification, GenericRangeField<T>... fields) {
		for (GenericRangeField<T> field : fields) {
			T min = field.getMin();
			T max = field.getMax();

			if (field.getMessage() == null) {
				String message = "Field " + field.getName() + " ";
				if (min != null && max != null) {
					message += "must be between " + min + " and " + max;
				} else if (min != null) {
					message += "must be greater than " + min;
				} else if (max != null) {
					message += "must be less than " + max;
				} else {
					message += "is required";
				}
				field.setMessage(message);
			}

			if (!(field.getValue() instanceof Comparable)) {
				if (field.isRequired()) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				}
				continue;
			}

			Comparable<T> value = (Comparable<T>) field.getValue();

			if ((min != null) && (value.compareTo(min) < 0)) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}

			if ((max != null) && (value.compareTo(max) > 0)) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}
		}
		return true;
	}

}
