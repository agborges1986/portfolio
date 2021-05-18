package proximity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class ObjectUtils {
	public static void map(Object dest, Object source) {
		for (int i = 0; i < dest.getClass().getFields().length; i++) {
			Field field = dest.getClass().getFields()[i];
			String name = "get" + (char) (field.getName().charAt(0) & ~32)
					+ field.getName().substring(1);
			try {
				Method m = source.getClass().getMethod(name);
				field.set(dest, m.invoke(source));
			} catch (NoSuchMethodException nsme) {
				continue;
			} catch (IllegalArgumentException | IllegalAccessException
					| InvocationTargetException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	public static <T> Collection<T> arrayToCollection(T[] array) {
		Collection<T> ret = new ArrayList<T>();
		for (int i = 0; i < array.length; i++) {
			ret.add(array[i]);
		}

		return ret;
	}

	public static Collection<Integer> arrayToCollection(int[] array) {
		Collection<Integer> ret = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++) {
			ret.add(array[i]);
		}

		return ret;
	}

}
