package orion.navigation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handle {

	private Controller controller;
	private Method pathMethod;
	private Map<String, String[]> parameterMap = new HashMap<>();
	private List<String> allowList;
	private List<String> denyList;

	public Handle(Controller controller, Method pathMethod, List<String> allowList, List<String> denyList) {
		this.controller = controller;
		this.pathMethod = pathMethod;
		this.allowList = allowList;
		this.denyList = denyList;
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

	public List<String> getAllowList() {
		return allowList;
	}

	public List<String> getDenyList() {
		return denyList;
	}

}
