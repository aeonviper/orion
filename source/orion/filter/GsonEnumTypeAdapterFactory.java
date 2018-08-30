package orion.filter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonEnumTypeAdapterFactory implements TypeAdapterFactory {

	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

		Class<T> rawType = (Class<T>) type.getRawType();
		if (!rawType.isEnum()) {
			return null;
		}

		return new TypeAdapter<T>() {
			public void write(JsonWriter writer, T object) throws IOException {
				if (object == null) {
					writer.nullValue();
					return;
				}

				writer.beginObject();
				try {
					Class clazz = object.getClass();
					BeanInfo info = Introspector.getBeanInfo(clazz);

					PropertyDescriptor[] propertyArray = info.getPropertyDescriptors();

					boolean hasData = false;
					for (PropertyDescriptor property : propertyArray) {
						String name = property.getName();
						if (!("class".equals(name) || "declaringClass".equals(name))) {
							Method accessor = property.getReadMethod();
							Object value = accessor.invoke(object);
							writer.name(name);
							writer.value(value.toString());
						}
					}
				} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				writer.endObject();
			}

			public T read(JsonReader reader) throws IOException {
				return null;
			}
		};
	}

}
