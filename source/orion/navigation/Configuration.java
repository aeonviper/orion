package orion.navigation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Configuration {

	private String controllerName;
	private Controller controller;

	private String path;
	private Pattern pathPattern;

	private String pathMethodName;
	private Method pathMethod;

	private String requestMethod;

	private List<String> pathParameterList = new ArrayList<>();

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Pattern getPathPattern() {
		return pathPattern;
	}

	public void setPathPattern(Pattern pathPattern) {
		this.pathPattern = pathPattern;
	}

	public String getPathMethodName() {
		return pathMethodName;
	}

	public void setPathMethodName(String pathMethodName) {
		this.pathMethodName = pathMethodName;
	}

	public Method getPathMethod() {
		return pathMethod;
	}

	public void setPathMethod(Method pathMethod) {
		this.pathMethod = pathMethod;
	}

	public List<String> getPathParameterList() {
		return pathParameterList;
	}

	public void setPathParameterList(List<String> pathParameterList) {
		this.pathParameterList = pathParameterList;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

}
