import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class KeyedCollection<K, V> extends AbstractCollection<V> {

	private final LinkedHashMap<K, V> map;

	public KeyedCollection() {
		map = new LinkedHashMap<>();
	}

	protected abstract K getKey(V value);

	@Override
	public Iterator<V> iterator() {
		return map.values().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean add(V value) {
		if (value == null) {
			throw new NullPointerException();
		}
		K key = getKey(value);
		if (map.containsKey(key)) {
			return false;
		} else {
			map.put(key, value);
			return true;
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean contains(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		@SuppressWarnings("unchecked")
		V value = (V) o;
		K key = getKey(value);
		return map.containsKey(key) && map.get(key).equals(value);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return super.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) o;
		return map.remove(getKey(value), value);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return super.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return super.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		return map.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return map.values().toArray(a);
	}
}
