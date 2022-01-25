package orion.validation.validator;

import java.net.MalformedURLException;
import java.net.URL;

import orion.controller.Notification;
import orion.validation.field.URLField;

public class URLValidator {

	public static boolean validate(Notification notification, URLField... fields) {
		for (URLField field : fields) {

			if (field.getMessage() == null) {
				field.setMessage("Field " + field.getName() + " must be a url");
			}

			if (!(field.getValue() instanceof String)) {
				if (field.isRequired()) {
					notification.addFieldError(field.getName(), field.getMessage());
					return false;
				}
				continue;
			}

			String value = (String) field.getValue();

			if (value.isEmpty()) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}

			if (!verifyUrl(value)) {
				notification.addFieldError(field.getName(), field.getMessage());
				return false;
			}

		}
		return true;
	}

	public static boolean verifyUrl(String url) {
		if (url == null) {
			return false;
		}

		if (url.startsWith("https://")) {
			// URL doesn't understand the https protocol, hack it
			url = "http://" + url.substring(8);
		}

		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
