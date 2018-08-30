package orion.navigation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Handle {

	private Controller controller;
	private Method pathMethod;
	private Map<String, String[]> parameterMap = new HashMap<>();

	public Handle(Controller controller, Method pathMethod) {
		this.controller = controller;
		this.pathMethod = pathMethod;
	}

	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	public Method getPathMethod() {
		return pathMethod;
	}

	public void setPathMethod(Method pathMethod) {
		this.pathMethod = pathMethod;
	}

	public Controller getController() {
		return controller;
	}

}
