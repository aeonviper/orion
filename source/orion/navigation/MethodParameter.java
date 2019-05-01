package orion.navigation;

import java.lang.annotation.Annotation;

public class MethodParameter {

	private Class annotationType;
	private Class type;
	private String name;
	private boolean list = false;

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public Class getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(Class annotationType) {
		this.annotationType = annotationType;
	}

}
