package orion.navigation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orion.annotation.Cookie;
import orion.annotation.Path;
import orion.annotation.PathSet;
import orion.annotation.Request;
import orion.annotation.Response;
import orion.annotation.Session;
import orion.core.Constant;
import orion.core.Utility;
import orion.view.View;

public class Navigation {

	public static final Pattern listParameterizedTypePatterm = Pattern.compile("^java.util.List<(.+)>$");
	// private static Configuration[] configurationArray = null;
	private static Map<String, List<Configuration>> configurationMap = new HashMap<>();

	private static void put(Map<String, List<Configuration>> configurationMap, String key, Configuration configuration) {
		List<Configuration> list = configurationMap.get(key);
		if (list == null) {
			configurationMap.put(key, list = new ArrayList<>());
		}
		list.add(configuration);
	}

	private static Map<String, List<Configuration>> configurationFile() {
		Map<String, List<Configuration>> configurationMap = new HashMap<>();

		List<String> lines = Utility.readFileResource(Constant.navigationConfigurationFile);
		for (String line : lines) {
			line = line.trim();
			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}
			String[] fields = line.split(Constant.navigationConfigurationFileDelimiter, 4);
			Configuration configuration = new Configuration();
			configuration.setControllerName(fields[0]);
			configuration.setPathMethodName(fields[1]);
			String[] parameterArray = fields[2].split(",");
			for (String parameter : parameterArray) {
				configuration.getPathParameterList().add(parameter.trim());
			}
			configuration.setPath(fields[3]);
			configuration.setPathPattern(Pattern.compile(configuration.getPath()));

			put(configurationMap, "any", configuration);
		}

		return configurationMap;
	}

	private static Map<String, List<Configuration>> configurationAnnotation(Class... classArray) {
		Map<String, List<Configuration>> configurationMap = new HashMap<>();
		for (Class controllerClass : classArray) {
			scan(configurationMap, controllerClass);
		}
		return configurationMap;
	}

	private static void scan(Map<String, List<Configuration>> configurationMap, Class controllerClass) {
		for (Method method : controllerClass.getDeclaredMethods()) {
			for (Annotation annotation : method.getAnnotations()) {
				if (annotation instanceof Path) {
					scan(configurationMap, controllerClass, method, annotation);
				} else if (annotation instanceof PathSet) {
					PathSet pathSet = (PathSet) annotation;
					for (Path annotationPath : pathSet.value()) {
						scan(configurationMap, controllerClass, method, annotationPath);
					}
				}
			}
		}
	}

	private static void scan(Map<String, List<Configuration>> configurationMap, Class controllerClass, Method method, Annotation annotation) {
		Path path = (Path) annotation;

		Configuration configuration = new Configuration();
		configuration.setControllerName(controllerClass.getCanonicalName());
		configuration.setPathMethodName(method.getName());
		configuration.setPathMethod(method);
		String[] parameterArray = path.name().split(",");
		for (String parameter : parameterArray) {
			configuration.getPathParameterList().add(parameter.trim());
		}
		configuration.setPath(path.value());
		configuration.setPathPattern(Pattern.compile(configuration.getPath()));
		configuration.setRequestMethod(path.method().isEmpty() ? "any" : path.method().toLowerCase());
		put(configurationMap, configuration.getRequestMethod(), configuration);
	}

	public static void controllerPathMethod(List<Configuration> configurationList) {
		Map<String, Boolean> controllerMap = new HashMap<>();

		for (Configuration configuration : configurationList) {

			String controllerClassName = configuration.getControllerName();
			if (!controllerMap.containsKey(controllerClassName)) {

				controllerMap.put(controllerClassName, Boolean.TRUE);

				Class controllerClass = null;
				try {
					controllerClass = Class.forName(controllerClassName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				if (controllerClass != null) {

					Controller controller = new Controller(controllerClass);

					for (Configuration config : configurationList) {
						if (config.getControllerName().equals(controllerClassName)) {
							config.setController(controller);
						}
					}

					Map<Annotation, List<Method>> annotationMethodMap = new HashMap<>();

					for (Method method : controllerClass.getMethods()) {

						Annotation annotation = null;
						if (method.getParameterCount() == 1) {
							if ((annotation = method.getAnnotation(Request.class)) != null) {
							} else if ((annotation = method.getAnnotation(Response.class)) != null) {
							} else if ((annotation = method.getAnnotation(Session.class)) != null) {
							} else if ((annotation = method.getAnnotation(Cookie.class)) != null) {
							}
						}

						if (method.getParameters().length == 0) {
							if (View.class.equals(method.getReturnType())) {
								for (Configuration config : configurationList) {
									if (config.getPathMethod() == null) {
										if (config.getControllerName().equals(controllerClassName)) {
											if (config.getPathMethodName().equals(method.getName())) {
												config.setPathMethod(method);
											}
										}
									}
								}
							}
						} else {
							if (annotation != null) {
								List<Method> methodList = annotationMethodMap.get(annotation);
								if (methodList == null) {
									annotationMethodMap.put(annotation, methodList = new ArrayList<>());
								}
								methodList.add(method);
							} else {
								List<MethodParameter> methodParameterList = new ArrayList<>();
								controller.getMethodParameterListMap().put(method, methodParameterList);
								for (Parameter parameter : method.getParameters()) {
									MethodParameter methodParameter = new MethodParameter();
									methodParameterList.add(methodParameter);

									if (parameter.getType().equals(java.util.List.class)) {
										Matcher matcher;
										if ((matcher = listParameterizedTypePatterm.matcher(parameter.getParameterizedType().getTypeName())).matches()) {
											try {
												methodParameter.setList(true);
												methodParameter.setType(Class.forName(matcher.group(1)));
											} catch (ClassNotFoundException e) {
												e.printStackTrace();
											}
										}
									} else {
										methodParameter.setType(parameter.getType());
									}

									for (Annotation parameterAnnotation : parameter.getAnnotations()) {
										if (parameterAnnotation instanceof orion.annotation.Parameter) {
											methodParameter.setName(((orion.annotation.Parameter) parameterAnnotation).value());
											methodParameter.setAnnotationType(orion.annotation.Parameter.class);
											break;
										} else if (parameterAnnotation instanceof orion.annotation.Request) {
											methodParameter.setName(((orion.annotation.Request) parameterAnnotation).value());
											methodParameter.setAnnotationType(orion.annotation.Request.class);
											break;
										} else if (parameterAnnotation instanceof orion.annotation.Response) {
											methodParameter.setAnnotationType(orion.annotation.Response.class);
											break;
										} else if (parameterAnnotation instanceof orion.annotation.Session) {
											methodParameter.setName(((orion.annotation.Session) parameterAnnotation).value());
											methodParameter.setAnnotationType(orion.annotation.Session.class);
											break;
										} else if (parameterAnnotation instanceof orion.annotation.Cookie) {
											methodParameter.setName(((orion.annotation.Cookie) parameterAnnotation).value());
											methodParameter.setAnnotationType(orion.annotation.Cookie.class);
											break;
										}
									}
								}
							}
						}

					}

					controller.setAnnotationMethodMap(annotationMethodMap);
				}
			}
		}
	}

	public static void configuration(Class... classArray) {
		configurationMap.putAll(configurationFile());
		configurationMap.putAll(configurationAnnotation(classArray));
		List<Configuration> list = new ArrayList<>();
		for (List<Configuration> configurationList : configurationMap.values()) {
			controllerPathMethod(configurationList);
			list.addAll(configurationList);
		}
		Collections.sort(list, new java.util.Comparator<Configuration>() {
			public int compare(Configuration o1, Configuration o2) {
				if (o1.getPath().equals(o2.getPath())) {
					return o1.getRequestMethod().compareTo(o2.getRequestMethod());
				}
				return o1.getPath().compareTo(o2.getPath());
			}
		});
		for (Configuration configuration : list) {
			System.out.println(configuration.getPath() + " " + configuration.getRequestMethod());
		}
	}

	public static Handle controllerFor(HttpServletRequest request, HttpServletResponse response) {
		String path = Utility.getPath(request);
		String requestMethod = request.getMethod().toLowerCase();
		Matcher matcher;
		List<List<Configuration>> configurationListToCheck = new ArrayList<>();

		configurationListToCheck.add(configurationMap.get(requestMethod));
		configurationListToCheck.add(configurationMap.get("any"));

		for (List<Configuration> configurationList : configurationListToCheck) {
			if (configurationList != null) {
				for (Configuration configuration : configurationList) {
					if ((matcher = configuration.getPathPattern().matcher(path)).matches()) {
						Handle handle = new Handle(configuration.getController(), configuration.getPathMethod());
						for (int i = 1; i <= matcher.groupCount(); i++) {
							handle.getParameterMap().put(configuration.getPathParameterList().get(i - 1), new String[] { matcher.group(i) });
						}
						return handle;
					}
				}
			}
		}
		return null;
	}

}
