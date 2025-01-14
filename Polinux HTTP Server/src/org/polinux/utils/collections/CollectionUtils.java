package org.polinux.utils.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CollectionUtils {

	public static final <T> T get(Iterable<T> iter, T obj) {
		synchronized (iter) {
			for (T o : iter) {
				if (Objects.equals(o, obj))
					return o;
			}
		}
		return null;
	}

	public static final <T> boolean contains(Iterable<T> iter, T obj) {
		/* Cannot be called. What if {@param obj} is equal to {@code null}? */
		// return get(iter, obj) != null;

		synchronized (iter) {
			for (T o : iter) {
				if (privateEqualsReal(o, obj))
					return true;
			}
		}
		return false;
	}

	private static <T> boolean privateEqualsReal(T o, T obj) {
		return (o == obj) || (o == null && obj == null);
	}

	public static final <T> boolean startsWithString(Iterable<T> iter, Object obj) {
		synchronized (iter) {
			for (T o : iter) {
				if (String.valueOf(o).startsWith(String.valueOf(obj)))
					return true;
			}
		}
		return false;
	}

	public static final <T> boolean startsWith(Iterable<T> iter, T obj) {
		synchronized (iter) {
			for (T o : iter) {
				if (Objects.equals(o, obj))
					return true;
				break;
			}
		}
		return false;
	}

	public static final <T> boolean endsWithString(Iterable<T> iter, Object obj) {
		synchronized (iter) {
			for (T o : iter) {
				if (String.valueOf(o).endsWith(String.valueOf(obj)))
					return true;
			}
		}
		return false;
	}

	public static final <T> boolean endsWith(Iterable<T> iter, T obj) {
		synchronized (iter) {
			Iterator<T> i = iter.iterator();
			int index = -1;
			int lastEnd = -1;

			while (i.hasNext()) {
				index++;
				if (Objects.equals(i.next(), obj))
					lastEnd = index;

			}

			if (lastEnd <= -1)
				return false;

			if (index != lastEnd)
				return false;
		}
		return true;
	}

	public static final <T> Set<T> toSet(Iterable<T> iter) {
		if (iter != null && iter instanceof Set) {
			return (Set<T>) iter;
		}
		synchronized (iter) {
			Set<T> set = new LinkedHashSet<T>();

			for (T item : iter) {
				set.add(item);
			}

			return set;
		}
	}

	public static final <T> List<T> toList(Iterable<T> iter) {
		if (iter != null && iter instanceof List) {
			return (List<T>) iter;
		}

		synchronized (iter) {
			List<T> list = new LinkedList<T>();

			for (T item : iter) {
				list.add(item);
			}

			return list;
		}
	}

	public static final <T> List<T> toList(T[] iter) {
		synchronized (iter) {
			List<T> list = new LinkedList<T>();

			for (int i = 0; i < iter.length; i++) {
				T item = iter[i];
				list.add(item);
			}

			return list;
		}
	}

	public static final <T> Set<T> toSet(T[] iter) {
		synchronized (iter) {
			Set<T> set = new LinkedHashSet<T>();

			for (int i = 0; i < iter.length; i++) {
				T item = iter[i];
				set.add(item);
			}

			return set;
		}
	}

	public static final <T> T[] toArray(Iterable<T> iter, Class<T> clazz) {
		synchronized (iter) {
			List<T> list = toList(iter);

			return (T[]) Arrays.copyOfRange(list.toArray(), 0, list.size(),
					(Class<? extends T[]>) Array.newInstance(clazz, 0).getClass());
		}
	}

	public static final <T> Integer size(Iterable<T> iter) {
		if (iter instanceof Collection)
			return ((Collection<T>) iter).size();

		synchronized (iter) {
			int size = 0;

			for (T item : iter) {
				size++;
			}

			return size;
		}
	}

}
