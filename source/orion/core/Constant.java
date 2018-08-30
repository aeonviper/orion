package orion.core;

public class Constant {

	public static final String sessionKey = "orion-session-key";
	public static final String model = "model";
	public static final String navigationConfigurationFileDelimiter = "\\|";
	public static final String applicationConfigurationFile = "application.configuration";
	public static final String navigationConfigurationFile = "navigation.configuration";

	private static String contextPath = "";

	public static String getContextPath() {
		return contextPath;
	}

	public static void setContextPath(String contextPath) {
		Constant.contextPath = contextPath;
	}

}
