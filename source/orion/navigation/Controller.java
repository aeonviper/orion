package orion.navigation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

	private Class controllerClass;

	private Map<Annotation, List<Method>> annotationMethodMap = Collections.unmodifiableMap(new HashMap<>());

	private Map<Method, List<MethodParameter>> methodParameterListMap = new HashMap<>();

	public Controller(Class controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Class getControllerClass() {
		return controllerClass;
	}

	public Map<Annotation, List<Method>> getAnnotationMethodMap() {
		return annotationMethodMap;
	}

	public void setAnnotationMethodMap(Map<Annotation, List<Method>> annotationMethodMap) {
		for (Map.Entry<Annotation, List<Method>> entry : annotationMethodMap.entrySet()) {
			entry.setValue(Collections.unmodifiableList(entry.getValue()));
		}
		this.annotationMethodMap = Collections.unmodifiableMap(annotationMethodMap);
	}

	public Map<Method, List<MethodParameter>> getMethodParameterListMap() {
		return methodParameterListMap;
	}

}
