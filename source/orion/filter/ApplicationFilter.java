package orion.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.annotation.Response;
import orion.annotation.Session;
import orion.configuration.Configuration;
import orion.controller.Attachment;
import orion.core.BeanUtility;
import orion.core.Constant;
import orion.core.Core;
import orion.core.Utility;
import orion.navigation.Handle;
import orion.navigation.MethodParameter;
import orion.navigation.Navigation;
import orion.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApplicationFilter implements Filter {

	protected String characterEncoding = "UTF-8";
	protected Gson gson = new Gson();
	protected Gson gsonEnumBean = new GsonBuilder().registerTypeAdapterFactory(new GsonEnumTypeAdapterFactory()).create();

	public void init(FilterConfig config) throws ServletException {
		String characterEncoding = config.getInitParameter("characterEncoding");
		if (characterEncoding != null) {
			this.characterEncoding = characterEncoding;
		}
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String path = Utility.getPath(request);

		Handle handle = Navigation.controllerFor(request, response);
		if (handle == null) {

			chain.doFilter(servletRequest, servletResponse);

		} else {

			if (request.getCharacterEncoding() == null) {
				request.setCharacterEncoding(characterEncoding);
			}

			Object instance = Core.getInjector().getInstance(handle.getController().getControllerClass());

			for (Map.Entry<Annotation, List<Method>> entry : handle.getController().getAnnotationMethodMap().entrySet()) {
				try {

					if (entry.getKey() instanceof Request) {
						Request annotation = (Request) entry.getKey();
						for (Method method : entry.getValue()) {
							Class[] parameterTypes = method.getParameterTypes();
							if (parameterTypes.length == 1) {
								if (annotation.value() == null || annotation.value().isEmpty()) {
									Class parameterType = parameterTypes[0];
									if (parameterType.isInstance(request)) {
										method.invoke(instance, new Object[] { request });
									} else if (request.getAttribute(parameterType.getCanonicalName()) != null) {
										method.invoke(instance, new Object[] { request.getAttribute(parameterType.getCanonicalName()) });
									}
								} else {
									method.invoke(instance, new Object[] { request.getAttribute(annotation.value()) });
								}
							}
						}
					} else if (entry.getKey() instanceof Response) {
						for (Method method : entry.getValue()) {
							method.invoke(instance, new Object[] { response });
						}
					} else if (entry.getKey() instanceof Session) {
						Session annotation = (Session) entry.getKey();
						for (Method method : entry.getValue()) {
							Class[] parameterTypes = method.getParameterTypes();
							if (parameterTypes.length == 1) {
								HttpSession session = request.getSession(false);
								if (session != null) {
									if (annotation.value() == null || annotation.value().isEmpty()) {
										Class parameterType = parameterTypes[0];
										if (parameterType.isInstance(session)) {
											method.invoke(instance, new Object[] { session });
										} else if (session.getAttribute(parameterType.getCanonicalName()) != null) {
											method.invoke(instance, new Object[] { session.getAttribute(parameterType.getCanonicalName()) });
										}
									} else {
										method.invoke(instance, new Object[] { session.getAttribute(annotation.value()) });
									}
								}
							}
						}
					} else if (entry.getKey() instanceof Cookie) {
						Cookie annotation = (Cookie) entry.getKey();
						String name = annotation.value();
						String value = null;
						if (name != null && !name.isEmpty()) {
							for (javax.servlet.http.Cookie cookie : request.getCookies()) {
								if (name.equals(cookie.getName())) {
									// more than 1 value ?
									value = cookie.getValue();
								}
							}
						}
						for (Method method : entry.getValue()) {
							Class[] parameterTypes = method.getParameterTypes();
							if (parameterTypes.length == 1) {
								if (parameterTypes[0] == String.class) {
									method.invoke(instance, new Object[] { value });
								}
							}
						}
					}

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			Map<String, String[]> parameterMap = new HashMap<>();
			for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				boolean valid = false;
				for (String v : entry.getValue()) {
					if (v.length() > 0) {
						valid = true;
						break;
					}
				}
				if (valid) {
					parameterMap.put(entry.getKey(), entry.getValue());
				}
			}

			parameterMap.putAll(handle.getParameterMap());

			String contentType = request.getContentType();

			Map<String, Object> bodyMap = null;
			if (contentType != null && contentType.toLowerCase().contains("application/json")) {
				bodyMap = (Map<String, Object>) gson.fromJson(request.getReader(), Map.class);
				if (bodyMap != null) {
					for (Map.Entry<String, Object> entry : bodyMap.entrySet()) {
						try {
							Class propertyType = PropertyUtils.getPropertyType(instance, entry.getKey());
							if (propertyType != null) {
								Object property = gson.fromJson(gson.toJson(entry.getValue()), propertyType);
								BeanUtility.instance().setProperty(instance, entry.getKey(), property);
							}
						} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
							e.printStackTrace();
						}
					}
				}
			}

			if (contentType != null && contentType.toLowerCase().contains("multipart")) {
				FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(request.getServletContext());
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setFileCleaningTracker(fileCleaningTracker);
				ServletFileUpload upload = new ServletFileUpload(factory);

				try {
					Map<String, List<String>> fileItemMap = new HashMap<>();
					List<FileItem> fileItemList = upload.parseRequest(request);
					Iterator<FileItem> fileItemIterator = fileItemList.iterator();
					while (fileItemIterator.hasNext()) {
						FileItem fileItem = fileItemIterator.next();
						if (fileItem.isFormField()) {
							String name = fileItem.getFieldName();
							String value = fileItem.getString();

							List<String> valueList = fileItemMap.get(name);
							if (valueList != null) {
								fileItemMap.get(name).add(value);
							} else {
								fileItemMap.put(name, valueList = new ArrayList<>());
								valueList.add(value);
							}
						} else {
							if (fileItem.getSize() != 0) {
								try {
									PropertyUtils.setProperty(instance, fileItem.getFieldName(), new Attachment(fileItem.getName(), fileItem.getContentType(), fileItem.getSize(), fileItem));
								} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

								}
							}
						}
					}
					for (Map.Entry<String, List<String>> entry : fileItemMap.entrySet()) {
						parameterMap.put(entry.getKey(), entry.getValue().toArray(new String[0]));
					}

				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			}

			try {
				BeanUtility.instance().populate(instance, parameterMap);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

			request.setAttribute(Constant.model, instance);

			Throwable throwable = null;
			Method pathMethod = handle.getPathMethod();
			View view = null;
			if (pathMethod != null) {
				try {
					if (pathMethod.getParameters().length == 0) {
						view = (View) pathMethod.invoke(instance, new Object[0]);
					} else {
						List parameterList = new ArrayList<>();
						for (MethodParameter methodParameter : handle.getController().getMethodParameterListMap().get(pathMethod)) {
							parameterList.add(evaluate(methodParameter, parameterMap, bodyMap, request, response));
						}
						view = (View) pathMethod.invoke(instance, parameterList.toArray());
					}
				} catch (Throwable t) {
					throwable = t;
				}
			} else {
				System.err.println("Warning: No path method for " + path);
			}

			if (throwable != null) {
				if (throwable instanceof InvocationTargetException) {
					throwable = ((InvocationTargetException) throwable).getTargetException();
				}
				throw new RuntimeException(throwable);
			}

			if (view != null) {
				if (view.getStatusCode() != null) {
					response.setStatus(view.getStatusCode());
				}
				if (View.Type.FORWARD == view.getType()) {
					response.setCharacterEncoding(characterEncoding);
					request.getRequestDispatcher((String) view.getValue()).forward(request, response);
				} else if (View.Type.REDIRECT == view.getType()) {
					response.sendRedirect(Constant.getContextPath() + (String) view.getValue());
				} else if (View.Type.JSON == view.getType()) {
					response.setCharacterEncoding(characterEncoding);
					response.setContentType("application/json");
					PrintWriter pw = response.getWriter();
					if (Boolean.TRUE.equals(view.getOptionAsBoolean("enumBean"))) {
						pw.println(gsonEnumBean.toJson(view.getValue()));
					} else {
						pw.println(gson.toJson(view.getValue()));
					}
				}
			} else {
			}
		}

	}

	private Object evaluate(MethodParameter parameter, Map<String, String[]> valueMap, Map<String, Object> bodyMap, HttpServletRequest request, HttpServletResponse response) {
		Object object = null;
		if (parameter.getAnnotationType() == orion.annotation.Parameter.class) {
			String name = parameter.getName();
			String[] valueArray = valueMap.get(name);
			if (valueArray != null) {
				if (valueArray.length >= 1) {
					if (parameter.isList()) {
						List list = new ArrayList<>();
						for (String value : valueArray) {
							list.add(gson.fromJson(value, parameter.getType()));
						}
						object = list;
					} else {
						String value = valueArray[0];
						object = gson.fromJson(value, parameter.getType());
					}
				}
			} else {
				// bean
				Map<String, String[]> beanValueMap = new HashMap<>();
				for (Map.Entry<String, String[]> entry : valueMap.entrySet()) {
					if (entry.getKey().startsWith(name + ".")) {
						String key = entry.getKey().substring((name + ".").length());
						beanValueMap.put(key, entry.getValue());
					}
				}
				if (!beanValueMap.isEmpty()) {
					object = Core.getInjector().getInstance(parameter.getType());
					try {
						BeanUtility.instance().populate(object, beanValueMap);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (parameter.getAnnotationType() == orion.annotation.Body.class) {
			if (bodyMap != null) {
				Object value = bodyMap.get(parameter.getName());
				if (value != null) {
					if (parameter.isList()) {
						if (value instanceof List) {
							List list = new ArrayList<>();
							for (Object o : (List) value) {
								list.add(gson.fromJson(gson.toJson(o), parameter.getType()));
							}
							object = list;
						}
					} else {
						object = gson.fromJson(gson.toJson(value), parameter.getType());
					}
				}
			}
		} else if (parameter.getAnnotationType() == orion.annotation.Request.class) {
			if (parameter.getName() == null || parameter.getName().isEmpty()) {
				if (parameter.getType().isInstance(request)) {
					object = request;
				} else if (request.getAttribute(parameter.getType().getCanonicalName()) != null) {
					object = request.getAttribute(parameter.getType().getCanonicalName());
				}
			} else {
				object = request.getAttribute(parameter.getName());
			}
		} else if (parameter.getAnnotationType() == orion.annotation.Response.class) {
			object = response;
		} else if (parameter.getAnnotationType() == orion.annotation.Session.class) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				if (parameter.getName() == null || parameter.getName().isEmpty()) {
					if (parameter.getType().isInstance(session)) {
						object = session;
					} else if (session.getAttribute(parameter.getType().getCanonicalName()) != null) {
						session.getAttribute(parameter.getType().getCanonicalName());
					}
				} else {
					object = session.getAttribute(parameter.getName());
				}
			}
		} else if (parameter.getAnnotationType() == orion.annotation.Cookie.class) {
			String name = parameter.getName();
			if (name != null && !name.isEmpty()) {
				for (javax.servlet.http.Cookie cookie : request.getCookies()) {
					if (name.equals(cookie.getName())) {
						// more than 1 value ?
						object = cookie.getValue();
					}
				}
			}
		}
		return object;
	}

}
