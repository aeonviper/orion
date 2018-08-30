package orion.configuration;

import java.util.List;

import orion.core.Constant;
import orion.core.Utility;

public class Configuration {

	private static String exceptionView = "exceptionView";
	private static String exceptionViewValue = "/WEB-INF/view/exception.jsp";

	private static void configurationFile() {

		List<String> lines = Utility.readFileResource(Constant.applicationConfigurationFile);
		for (String line : lines) {
			line = line.trim();
			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}
			String[] fields = line.split("=", 2);
			String key = fields[0];
			String value = fields[1];

			if (exceptionView.equals(key) && value != null && !value.trim().isEmpty()) {
				exceptionViewValue = value;
			}

		}

	}

	public static String getExceptionViewValue() {
		return exceptionViewValue;
	}

}
